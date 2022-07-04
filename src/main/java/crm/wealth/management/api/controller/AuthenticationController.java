package crm.wealth.management.api.controller;

import crm.wealth.management.api.form.LoginForm;
import crm.wealth.management.api.response.ApiResponse;
import crm.wealth.management.api.response.AuthenticationResponse;
import crm.wealth.management.config.JwtTokenUtil;
import crm.wealth.management.model.Token;
import crm.wealth.management.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@CrossOrigin
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private HttpServletRequest servletRequest;

	@Value("${limit.attempt}")
	private int limitAttempt;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginForm authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		List<String> ips = new ArrayList<>();
		Token tokenEntity;
		Optional<Token> tokenOptional = tokenRepository.findById(userDetails.getUsername());

		String ipAddress = servletRequest.getRemoteAddr();
		if (tokenOptional.isPresent()) {
			tokenEntity = tokenOptional.get();
			ips = Arrays.asList(tokenEntity.getIpClients().split(";"));
			if (ips.size() >= limitAttempt && !ips.contains(ipAddress)) {
				return ResponseEntity
						.status(HttpStatus.UNAUTHORIZED.value())
						.body(new AuthenticationResponse("Maximum attempt into system is " + limitAttempt + " time per one account"));
			}
		} else {
			tokenEntity = new Token();
			tokenEntity.setUsername(userDetails.getUsername());
			tokenEntity.setToken(token);
			String createdStr = jwtTokenUtil.getCreatedFromToken(token);
			long created = Long.parseLong(createdStr);
			tokenEntity.setCreated(created);
		}

		StringBuilder ipString = new StringBuilder();
		for (String ip : ips) {
			ipString.append(ip).append(";");
		}
		if (!ips.contains(ipAddress)) {
			ipString.append(ipAddress);
			tokenEntity.setIpClients(ipString.toString());
		}

		tokenRepository.save(tokenEntity);
		return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Login successfully", tokenEntity).getBody());
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@PostMapping("/oauth/logout")
	public ApiResponse logout(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username;
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
			request.logout();
			Optional<Token> tokenOptional = tokenRepository.findById(username);
			if (tokenOptional.isPresent()) {
				tokenRepository.deleteById(username);
			}
			new SecurityContextLogoutHandler().logout(request, response, auth);
		} catch (ServletException e) {
			return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "BAD REQUEST");
		}
		return new ApiResponse(HttpStatus.OK.value(), "Logged out");
	}
}
