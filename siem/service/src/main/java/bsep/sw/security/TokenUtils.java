package bsep.sw.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class TokenUtils {

    private static final String CREATED = "created";
    private static final String SUBJECT = "sub";
    private static final String ROLE = "role";
    private static final String LOGIN_TYPE = "login_type";

    public enum LoginType {FACEBOOK, BASIC}

    @Value("${security.token.secret}")
    private String secret;

    @Value("${security.token.expiration}")
    private Long expiration;

    private final UserDetailsService userDetailsService;

    @Autowired
    public TokenUtils(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Extracts username from token.
     *
     * @param token authentication token
     * @return username
     */
    public String getUsernameFromToken(final String token) throws Exception {
        final Claims claims = this.getClaimsFromToken(token);
        return claims.get(SUBJECT, String.class);
    }

    /**
     * Extracts Claims from token.
     *
     * @param token authentication token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    private Date generateExpirationDate(Long tokenExpiration) {
        if (tokenExpiration != null) {
            return new Date(System.currentTimeMillis() + tokenExpiration);
        }
        return new Date(System.currentTimeMillis() + this.expiration);
    }

    /**
     * Generates token based on details of the user.
     *
     * @param userDetails UserDetails
     * @return encrypted string - token
     */
    public String generateToken(UserDetails userDetails, Long tokenExpiration, LoginType loginType) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(SUBJECT, userDetails.getUsername());
        // Set Role of User to token. Our user has only one role.
        claims.put(ROLE, userDetails.getAuthorities().toArray()[0]);
        claims.put(CREATED, this.generateCurrentDate());
        claims.put(LOGIN_TYPE, loginType);
        return this.generateToken(claims, tokenExpiration);
    }

    private String generateToken(Map<String, Object> claims, Long tokenExpiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get(SUBJECT).toString())
                .setExpiration(this.generateExpirationDate(tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    public String refreshToken(Claims claims) {
        LoginType loginType = LoginType.valueOf(claims.get(LOGIN_TYPE).toString());
        if (loginType == LoginType.FACEBOOK) {
            // TODO: Implement logic for fb redirection
            return null;
        } else if (loginType == LoginType.BASIC) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(claims.get(SUBJECT).toString());
            if (userDetails != null) {
                claims.put(CREATED, this.generateCurrentDate());
                return generateToken(claims, null);
            }
        }
        return null;
    }

}
