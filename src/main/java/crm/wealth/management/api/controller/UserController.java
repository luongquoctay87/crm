package crm.wealth.management.api.controller;


import crm.wealth.management.api.form.UserForm;
import crm.wealth.management.api.response.ApiResponse;
import crm.wealth.management.api.response.ErrorResponse;
import crm.wealth.management.api.response.ViewResponse;
import crm.wealth.management.model.AppUser;
import crm.wealth.management.dto.UserDTO;
import crm.wealth.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ApiResponse getUserList(
            @RequestParam(name = "search", defaultValue = "") Optional<String> _search,
            @RequestParam(name = "pageNo", defaultValue = "1") Optional<Integer> _pageNo,
            @RequestParam(name = "pageSize", defaultValue = "20") Optional<Integer> _pageSize) {
        log.info("Request api GET api/v1/users");

        List<UserDTO> users = userService.findAll(_search.get(), _pageNo.get(), _pageSize.get())
                .stream().map(user -> mapper.map(user, UserDTO.class)).collect(Collectors.toList());

        long count = userService.count();
        ViewResponse view = ViewResponse.builder()
                .pageNo(_pageNo.get())
                .pageSize(_pageSize.get())
                .total(count)
                .items(users)
                .build();

        return new ApiResponse(HttpStatus.OK.value(), "users", view);
    }

    @GetMapping("/{id}")
    public ApiResponse getUser(@PathVariable("id") Long _id) {
        log.info("Request api GET api/v1/users/{}", _id);

        AppUser user = userService.getById(_id);
        UserDTO dto = mapper.map(user, UserDTO.class);

        return new ApiResponse(HttpStatus.OK.value(), "user", dto);
    }

    // @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    public ApiResponse createUser(@RequestBody UserForm form) {
        log.info("Request api POST api/v1/users");

        if (userService.isUserExist(form.getUsername())) {
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Username is exists");
        }

        if (!form.getPassword().equals(form.getRePassword())) {
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Password not match");
        }
        try {
            AppUser newUser = userService.addUser(form);
            return new ApiResponse(HttpStatus.OK.value(), "Create user successful", newUser.getId());
        } catch (Exception e){
            log.info("Can not create user");
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Create user fail");
        }
    }

    @PutMapping
    public ApiResponse updateUser(@RequestBody UserForm form) {
        log.info("Request api PUT api/v1/users/{}", form.getId());

        if (userService.isUserExist(form.getUsername())) {
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Username is exists");
        }

        AppUser user = userService.getById(form.getId());
        if (user == null) {
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "User not found");
        }
        try {
            userService.updateUser(form);
            return new ApiResponse(HttpStatus.OK.value(), "Update user successful", form.getId());
        } catch (Exception e){
            log.info("Can not update userId ={}", form.getId());
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Update user fail");
        }
    }

    @PatchMapping("/changePassword/{id}")
    public ApiResponse changPassword(@RequestParam String password, @RequestParam String rePassword, @PathVariable("id") Long _id) {
        log.info("Request api PATCH api/v1/users/changePassword/{}", _id);

        AppUser user = userService.getById(_id);
        if (user == null) {
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "User not found");
        }
        if (!password.equals(rePassword)) {
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Password not match");
        }
        try {
            user.setPassword(encoder.encode(password));
            userService.changePassword(user);
            return new ApiResponse(HttpStatus.OK.value(), "Change password successful", _id);
        } catch (Exception e){
            log.info("Can not change password, userId ={}", _id);
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Change password fail");
        }
    }

    // @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteUser(@PathVariable(value = "id") Long _id) {
        log.info("Request api DELETE api/v1/users/{}", _id);

        try {
            userService.delete(_id);
            return new ApiResponse(HttpStatus.OK.value(), "Delete user successful");
        } catch (Exception e){
            log.info("Can not delete user, userId ={}", _id);
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Delete user fail");
        }
    }

    @PutMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestParam String email) {
        try {
            log.info("Request forgot password, email = {}, ", email);
            return userService.forgotPassword(email);
        } catch (Exception e) {
            log.error("Can not execute forgot password");
            log.error(e.getMessage());
            return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Forgot password fail");
        }
    }
}
