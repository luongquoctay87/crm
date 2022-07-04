package crm.wealth.management.config;


import com.google.gson.Gson;
import crm.wealth.management.model.ContentTrace;
import crm.wealth.management.model.Token;
import crm.wealth.management.repository.TokenRepository;
import crm.wealth.management.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private TokenRepository tokenRepository;

	private ContentTrace contentTrace;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
		contentTrace = new ContentTrace();
		contentTrace.setTimestamp(LocalDateTime.now());
		try {
			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
				responseWrapper.setStatus(HttpServletResponse.SC_OK);
			}

			final String requestTokenHeader = request.getHeader("Authorization");

			String username = null;
			String jwtToken = null;
			// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} /*else {
				logger.warn("JWT Token does not begin with Bearer String");
			}*/

			//Once we get the token validate it.
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = this.userService.loadUserByUsername(username);

				String createdStr = jwtTokenUtil.getCreatedFromToken(jwtToken);
				long created = Long.parseLong(createdStr);
				tokenRepository.findByUsernameAndCreated(username, created)
						.orElseThrow(() -> new AuthenticationException("JWT Token has invalid"));
				// if token is valid configure Spring Security to manually set authentication
				if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			chain.doFilter(requestWrapper, responseWrapper);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			printErrorResponse(response, "Unable to get JWT Token");
		} catch (ExpiredJwtException e) {
			log.error(e.getMessage());
			Claims claims = e.getClaims();
			String usernameToken = claims.get("username").toString();
			Optional<Token> token = tokenRepository.findById(usernameToken);
			token.ifPresent(value -> tokenRepository.delete(value));
			printErrorResponse(response, "JWT Token has expired");
		} catch (AuthenticationException e) {
			log.error(e.getMessage());
			printErrorResponse(response, e.getMessage());
		}
		finally {
			afterRequest(requestWrapper, responseWrapper);
		}
	}

	private void printErrorResponse(HttpServletResponse response, String message) throws IOException {
		PrintWriter out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		Map<String, Object> hm = new HashMap<>();
		hm.put("message", message);
		out.print(parseObjectToString(hm));
		out.flush();
	}

	private void afterRequest(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException {
		String method = requestWrapper.getMethod();
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			contentTrace.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		}
		contentTrace.setTimeTaken(System.currentTimeMillis() - contentTrace.timestamp.atZone(ZoneId.systemDefault()).toEpochSecond());
		contentTrace.setMethod(method);
		contentTrace.setRemoteAddress(requestWrapper.getRemoteAddr());
		contentTrace.setUri(requestWrapper.getRequestURI());
		contentTrace.setHost(requestWrapper.getHeader("host"));
		contentTrace.setAuthorization(requestWrapper.getHeader("authorization"));
		contentTrace.setUserAgent(requestWrapper.getHeader("user-agent"));
		contentTrace.setReferer(requestWrapper.getHeader("referer"));
		contentTrace.setReqBody(getRequestPayload(requestWrapper));
		contentTrace.setStatus(responseWrapper.getStatusCode());
		contentTrace.setResBody(getResponsePayload(responseWrapper));
		responseWrapper.copyBodyToResponse();
		log.info("REQUEST-PAYLOAD --------------------- \n" + contentTrace.getReqBody());
		log.info("RESPONSE-PAYLOAD --------------------- \n" + contentTrace.getResBody());
	}

	private String parseObjectToString(Object object) {
		return new Gson().toJson(object);
	}

	private String getRequestPayload(ContentCachingRequestWrapper request) {
		ContentCachingRequestWrapper wrapper =
				WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				int length = buf.length;
				try {
					return new String(buf, 0, length, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException ex) {
					return "[unknown]";
				}
			}
		}
		return "";
	}

	private String getResponsePayload(ContentCachingResponseWrapper wrappedResponse) {
		try {
			if (wrappedResponse.getContentSize() <= 0) {
				return null;
			}
			return new String(wrappedResponse.getContentAsByteArray(), 0,
					wrappedResponse.getContentSize(),
					wrappedResponse.getCharacterEncoding());
		} catch (UnsupportedEncodingException e) {
			logger.error(
					"Could not read cached response body: " + e.getMessage());
			return null;
		}
	}

}
