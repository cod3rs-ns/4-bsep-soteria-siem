package bsep.sw.controllers;

import bsep.sw.domain.User;
import bsep.sw.util.AuthResponse;
import bsep.sw.security.TokenUtils;
import bsep.sw.services.UserService;
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
import java.net.URI;
import java.net.URISyntaxException;

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = tokenUtils.generateToken(userDetails, null, TokenUtils.LoginType.BASIC);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) throws URISyntaxException {
        if (user.getId() != null) {
            return ResponseEntity.badRequest().body("User have an id already.");
        }

        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body(String.format("User with username: %s  already exists.", user.getUsername()));
        }

        User result = userService.save(user);

        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                .body(result);
    }

    @RequestMapping("/users/me")
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<User> me() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return new ResponseEntity<>(userService.getUserByUsername(currentPrincipalName), HttpStatus.OK);
    }
}
