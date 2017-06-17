package bsep.sw.security;

import bsep.sw.util.FacebookUserResponse;
import bsep.sw.util.HttpHeadersUtil;
import bsep.sw.util.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import static bsep.sw.util.FacebookConstants.*;


public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = Logger.getLogger(AuthenticationTokenFilter.class);

    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;
    private final String userInfoUrl;

    public AuthenticationTokenFilter(final UserDetailsService userDetailsService,
                                     final TokenUtils tokenUtils,
                                     final String userInfoUrl) {
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
        this.userInfoUrl = userInfoUrl;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        String username = null;
        String authToken = httpRequest.getHeader(HttpHeadersUtil.X_AUTH_TOKEN.getName());

        if (authToken != null && authToken.startsWith("bearer")) {
            final String fbToken = authToken.substring(6).trim();
            try {
                final FacebookUserResponse user = getFbUserInfo(fbToken);
                username = "fb_" + user.getId();
            } catch (final URISyntaxException e) {
                log.debug("Can't fetch fb user", e);
            }
        } else {
            try {
                username = tokenUtils.getUsernameFromToken(authToken);
            } catch (final ExpiredJwtException ex) {
                // refresh token
                final Claims claims = ex.getClaims();
                authToken = tokenUtils.refreshToken(claims);
                username = claims.getSubject();
                httpResponse.setHeader(HttpHeadersUtil.X_AUTH_REFRESHED.getName(), HttpHeadersUtil.X_AUTH_REFRESHED.getValue());

                log.debug(String.format("Token refreshed for user: %s", username), ex);
            } catch (final Exception ex) {
                log.debug("Wrong token provided", ex);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        httpResponse.setHeader(HttpHeadersUtil.X_AUTH_TOKEN.getName(), authToken);
        chain.doFilter(request, httpResponse);
    }

    private FacebookUserResponse getFbUserInfo(final String accessToken) throws URISyntaxException, IOException {
        final URIBuilder builder = new URIBuilder(userInfoUrl);

        builder.addParameter(FIELDS, FIELDS_VALUE);
        builder.addParameter(ACCESS_TOKEN, accessToken);

        final String url = URLDecoder.decode(builder.build().toString(), "utf-8");
        final String data = RestClient.get(url);
        return new ObjectMapper().readValue(data, FacebookUserResponse.class);
    }
}
