package bsep.sw.controllers;


import bsep.sw.domain.User;
import bsep.sw.domain.UserRole;
import bsep.sw.security.TokenUtils;
import bsep.sw.services.UserService;
import bsep.sw.hateoas.user.AuthResponse;
import bsep.sw.util.FacebookTokenResponse;
import bsep.sw.util.FacebookUserResponse;
import bsep.sw.util.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.UUID;

import static bsep.sw.util.FacebookConstants.*;

@RestController
@RequestMapping("/facebook")
public class SocialLoginController {

    @Value("${spring.social.facebook.app-id}")
    private String appId;

    @Value("${spring.social.facebook.app-secret}")
    private String appSecret;

    @Value("${spring.social.facebook.dialog-url}")
    private String dialogUrl;

    @Value("${spring.social.facebook.redirect-uri}")
    private String redirectUri;

    @Value("${spring.social.facebook.access-token-url}")
    private String accessTokenUrl;

    @Value("${spring.social.facebook.user-info-url}")
    private String userInfoUrl;

    @Value("${spring.social.facebook.scope}")
    private String scope;

    private static final String pass = "default";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    @Autowired
    public SocialLoginController(final UserService userService, final AuthenticationManager authenticationManager,
                                 final UserDetailsService userDetailsService, final TokenUtils tokenUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @RequestMapping("/auth")
    public void loginViaFb(final HttpServletResponse http) throws Exception {
        final URIBuilder builder = new URIBuilder(dialogUrl);

        builder.addParameter(CLIENT_ID, appId);
        builder.addParameter(REDIRECT_URI, redirectUri);
        builder.addParameter(SCOPE, scope);
        builder.addParameter(STATE, UUID.randomUUID().toString());

        http.sendRedirect(builder.build().toString());
    }

    @RequestMapping("/access-token")
    public ResponseEntity<AuthResponse> redirectionCallback(
            @RequestParam(CODE) final String code,
            @RequestParam(STATE) final String state) throws Exception {
        if (code != null) {
            // TODO: check state to prevent CSRF attack
            // Exchange code to get access token
            final URIBuilder builder = new URIBuilder(accessTokenUrl);

            builder.addParameter(CLIENT_ID, appId);
            builder.addParameter(CLIENT_SECRET, appSecret);
            builder.addParameter(REDIRECT_URI, redirectUri);
            builder.addParameter(CODE, code);

            final String url = URLDecoder.decode(builder.toString(), "utf-8");
            final FacebookTokenResponse tokenResponse = new ObjectMapper().readValue(RestClient.get(url), FacebookTokenResponse.class);

            final FacebookUserResponse userResponse = getFbUserInfo(tokenResponse.getAccessToken());
            User fbUser = createOrFindFbUser(userResponse);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(fbUser.getUsername(), pass));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(fbUser.getUsername());
            String token = tokenUtils.generateToken(userDetails, tokenResponse.getExpiresIn(), TokenUtils.LoginType.FACEBOOK);

            return ResponseEntity.ok(AuthResponse.fromDomain(token, tokenResponse.getExpiresIn()));
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private FacebookUserResponse getFbUserInfo(final String accessToken) throws URISyntaxException, IOException {
        final URIBuilder builder = new URIBuilder(userInfoUrl);

        builder.addParameter(FIELDS, FIELDS_VALUE);
        builder.addParameter(ACCESS_TOKEN, accessToken);

        final String url = URLDecoder.decode(builder.build().toString(), "utf-8");
        return new ObjectMapper().readValue(RestClient.get(url), FacebookUserResponse.class);
    }

    private User createOrFindFbUser(final FacebookUserResponse userResponse) {
        // TODO: define default password and username
        User retUser = userService.getUserByUsername("fb_" + userResponse.getId());
        if (retUser == null) {
            retUser = new User();
            retUser.setEmail(userResponse.getEmail());
            retUser.setFirstName(userResponse.getFirstName());
            retUser.setLastName(userResponse.getLastName());
            retUser.setRole(UserRole.FACEBOOK);
            retUser.setUsername("fb_" + userResponse.getId());
            retUser.setPassword(pass);

            retUser = userService.save(retUser);
        }

        return retUser;
    }
}
