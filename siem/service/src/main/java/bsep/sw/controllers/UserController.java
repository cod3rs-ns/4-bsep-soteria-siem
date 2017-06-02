package bsep.sw.controllers;

import bsep.sw.domain.User;
import bsep.sw.hateoas.ErrorResponse;
import bsep.sw.hateoas.user.UserRequest;
import bsep.sw.hateoas.user.UserResponse;
import bsep.sw.security.TokenUtils;
import bsep.sw.services.UserService;
import bsep.sw.hateoas.user.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    @Autowired
    public UserController(final UserService userService, final AuthenticationManager authenticationManager,
                          final UserDetailsService userDetailsService, final TokenUtils tokenUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(path = "/users/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = tokenUtils.generateToken(userDetails, null, TokenUtils.LoginType.BASIC);

        return ResponseEntity.ok(AuthResponse.fromDomain(token, tokenUtils.getExpiration()));
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        final User user = userRequest.toDomain();

        if (userService.getUserByUsername(user.getUsername()) != null) {
            final ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.toString(),
                    "Already exist",
                    String.format("User with username: %s  already exists.", user.getUsername()));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.ok(UserResponse.fromDomain(userService.save(user)));
    }

    @RequestMapping("/users/me")
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<UserResponse> me() throws IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String currentPrincipalName = authentication.getName();

        return new ResponseEntity<>(UserResponse.fromDomain(userService.getUserByUsername(currentPrincipalName)), HttpStatus.OK);
    }
}
