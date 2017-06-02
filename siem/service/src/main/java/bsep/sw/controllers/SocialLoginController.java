package bsep.sw.controllers;


import bsep.sw.domain.User;
import bsep.sw.domain.UserRole;
import bsep.sw.hateoas.ErrorResponse;
import bsep.sw.hateoas.user.AuthResponse;
import bsep.sw.security.TokenUtils;
import bsep.sw.services.UserService;
import bsep.sw.util.FacebookTokenResponse;
import bsep.sw.util.FacebookUserResponse;
import bsep.sw.util.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import static bsep.sw.util.FacebookConstants.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("api/social")
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

    private final String pass = "default";

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

    @RequestMapping("/facebook/auth")
    public void loginViaFb(@RequestParam(value = STATE) String state, final HttpServletResponse http) throws Exception {
        final URIBuilder builder = new URIBuilder(dialogUrl);

        builder.addParameter(CLIENT_ID, appId);
        builder.addParameter(REDIRECT_URI, redirectUri);
        builder.addParameter(SCOPE, scope);
        builder.addParameter(STATE, state);

        http.sendRedirect(builder.build().toString());
    }

    @RequestMapping("/facebook/access-token")
    public ResponseEntity<?> redirectionCallback(@RequestParam(value = CODE, required = false) final String code,
                                                 @RequestParam(value = STATE) final String state,
                                                 @RequestParam(value = ERROR, required = false) final String error,
                                                 @RequestParam(value = ERROR_DESCRIPTION, required = false) final String errorDescription) throws Exception {
        if (code != null) {
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

        return new ResponseEntity<>(new ErrorResponse(String.valueOf(UNAUTHORIZED.value()), error, errorDescription), UNAUTHORIZED);
    }

    private FacebookUserResponse getFbUserInfo(final String accessToken) throws URISyntaxException, IOException {
        final URIBuilder builder = new URIBuilder(userInfoUrl);

        builder.addParameter(FIELDS, FIELDS_VALUE);
        builder.addParameter(ACCESS_TOKEN, accessToken);

        final String url = URLDecoder.decode(builder.build().toString(), "utf-8");
        return new ObjectMapper().readValue(RestClient.get(url), FacebookUserResponse.class);
    }

    private User createOrFindFbUser(final FacebookUserResponse userResponse) throws UnsupportedEncodingException {
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
