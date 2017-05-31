package bsep.sw.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${security.token.header}")
    private String tokenHeader;

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
        String authToken = httpRequest.getHeader(this.tokenHeader);

        String username;
        try {
            username = tokenUtils.getUsernameFromToken(authToken);
        } catch (ExpiredJwtException ex) {
            // refresh token
            final Claims claims = ex.getClaims();
            authToken = tokenUtils.refreshToken(claims);
            username = claims.getSubject();
            log.debug(String.format("Token refreshed for user: %s", username), ex);
        } catch (Exception ex) {
            log.debug("Wrong token provided", ex);
            username = null;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-AUTH-TOKEN", authToken);
        chain.doFilter(request, httpResponse);
    }
}
