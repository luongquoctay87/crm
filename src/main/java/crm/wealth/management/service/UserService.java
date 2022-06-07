package crm.wealth.management.service;

import crm.wealth.management.api.form.UserForm;
import crm.wealth.management.api.response.ApiResponse;
import crm.wealth.management.api.response.ErrorResponse;
import crm.wealth.management.config.ResourceNotFoundException;
import crm.wealth.management.model.AppUser;
import crm.wealth.management.repository.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private MailService mailService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AppUser appUser = userRepo.findByUsername(username);
		if (appUser == null) {
			throw new UsernameNotFoundException("AppUser not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), getAuthority(username));
	}

	private List<SimpleGrantedAuthority> getAuthority(String username) {
		List<String> roles = userRepo.findRoleByUsername(username);
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (String role: roles) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
			authorities.add(authority);
		}

		return authorities;
	}

	public List<AppUser> findAll(String keyword, int pageNo, int pageSize) {
		StringBuilder condition = new StringBuilder("%");
		condition.append(keyword);
		condition.append("%");

		if (pageNo > 0) pageNo = pageNo - 1;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<AppUser> pagedResult = userRepo.findAllUsers(condition.toString(), pageable);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<AppUser>();
		}
	}

	public AppUser getById(long id) {
		Optional<AppUser> user = userRepo.findById(id);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not found", "userId", id);
		}
		return user.get();
	}

	public long count() {
		return userRepo.count();
	}

	public AppUser addUser(UserForm form) {
		AppUser user = AppUser.builder()
				.firstName(form.getFirstName())
				.middleName(form.getMiddleName())
				.surname(form.getSurname())
				.fullName(form.getFullName())
				.phone(form.getPhone())
				.email(form.getEmail())
				.address(form.getAddress())
				.username(form.getUsername())
				.password(encoder.encode(form.getPassword()))
				.createdDate(new Date())
				.updatedDate(new Date())
				.build();

		return userRepo.save(user);
	}

	public AppUser updateUser(UserForm form) {
		AppUser user = getById(form.getId());
		user.setFirstName(form.getFirstName());
		user.setMiddleName(form.getMiddleName());
		user.setSurname(form.getSurname());
		user.setFullName(form.getFullName());
		user.setPhone(form.getPhone());
		user.setEmail(form.getEmail());
		user.setAddress(form.getAddress());
		user.setUsername(form.getUsername());
		user.setUpdatedDate(new Date());

		return userRepo.save(user);
	}

	public void delete(long id) {
		userRepo.deleteById(id);
	}

	public AppUser changePassword(AppUser user) {
		return userRepo.save(user);
	}

	public boolean isUserExist(String username) {
		AppUser user = userRepo.findByUsername(username);
		if (user != null)
			return true;
		else
			return false;
	}

	public ApiResponse forgotPassword(String email) {
		String password = RandomStringUtils.random(10, true, true);
		AppUser user = userRepo.findByEmail(email);
		if (user == null)
			return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Email " + email + " not found");
		user.setPassword(encoder.encode(password));
		mailService.sendSimpleMessage(email, "[CRM SYSTEM] - Forgot Password", "New password: " + password);
		userRepo.save(user);
		return new ApiResponse(HttpStatus.OK.value(), "Forgot password successful");
	}
}
