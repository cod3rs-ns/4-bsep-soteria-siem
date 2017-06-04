package bsep.sw.security;

import bsep.sw.util.HttpHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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


public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = Logger.getLogger(AuthenticationTokenFilter.class);

    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    public AuthenticationTokenFilter(final UserDetailsService userDetailsService,
                                     final TokenUtils tokenUtils) {
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authToken = httpRequest.getHeader(HttpHeaders.X_AUTH_TOKEN.getName());

        String username;
        try {
            username = tokenUtils.getUsernameFromToken(authToken);
        } catch (final ExpiredJwtException ex) {
            // refresh token
            final Claims claims = ex.getClaims();

            final TokenUtils.LoginType loginType = TokenUtils.LoginType.valueOf(claims.get(TokenUtils.getLoginType()).toString());
            if (loginType == TokenUtils.LoginType.FACEBOOK) {
                // TODO: Redirect to client login page
                final String redirectUrl = "http://www.google.ba" ;
                try {
                    httpResponse.sendRedirect(redirectUrl);
                } catch (final IOException e) {
                    log.debug(String.format("Can't redirect fb user to url %s", redirectUrl), e);
                }
                return;
            }

            authToken = tokenUtils.refreshToken(claims);
            username = claims.getSubject();
            httpResponse.setHeader(HttpHeaders.X_AUTH_REFRESHED.getName(), HttpHeaders.X_AUTH_REFRESHED.getValue());

            log.debug(String.format("Token refreshed for user: %s", username), ex);
        } catch (final Exception ex) {
            log.debug("Wrong token provided", ex);
            username = null;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        httpResponse.setHeader(HttpHeaders.X_AUTH_TOKEN.getName(), authToken);
        chain.doFilter(request, httpResponse);
    }
}
