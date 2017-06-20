package bsep.sw.configuration;

import bsep.sw.security.AuthenticationTokenFilter;
import bsep.sw.security.EntryPointUnauthorizedHandler;
import bsep.sw.security.TokenUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Web security configuration.
 */
@Component
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger log = Logger.getLogger(WebSecurityConfiguration.class);

    private final EntryPointUnauthorizedHandler unauthorizedHandler;
    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;
    private final String userInfoUrl;

    @Autowired
    public WebSecurityConfiguration(final EntryPointUnauthorizedHandler unauthorizedHandler,
                                    final UserDetailsService userDetailsService,
                                    final TokenUtils tokenUtils,
                                    @Value("${spring.social.facebook.user-info-url}") final String userInfoUrl) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
        this.userInfoUrl = userInfoUrl;
    }

    /**
     * Configures authentication.
     *
     * @param authenticationManagerBuilder authentication manager builder
     */
    @Autowired
    public void configureAuthentication(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        try {
            authenticationManagerBuilder
                    .userDetailsService(this.userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (final Exception e) {
            log.error("Exception in WebSecurityConfiguration.configureAuthentication();", e);
        }

    }

    /**
     * Creates password encoder.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Creates authentication token filter.
     *
     * @return AuthenticationTokenFilter
     * @throws Exception when wrong token is provided
     */
    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        final AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter(userDetailsService, tokenUtils, userInfoUrl);
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users**").permitAll();
        // Custom JWT based authentication
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.requiresChannel().anyRequest().requiresSecure();
    }

}
