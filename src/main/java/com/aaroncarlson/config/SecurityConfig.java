package com.aaroncarlson.config;

import com.aaroncarlson.security.CustomUserDetailsService;
import com.aaroncarlson.security.JwtAuthenticationEntryPoint;
import com.aaroncarlson.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @EnableWebSecurity - primary Spring Security annotation used to enable web security in a project
 * @EnableGlobalMethodSEcurity - enables method level security based on annotations
 * - secureEnabled: enables the @Secured annotation, this allows access to controller/services methods
 *   only if the User has at least one of the specified roles
 * - jsr250Enabled: enables the @RoleAllowed annotation
 * - prePostEnabled: enables more complex expression based access control syntax using @PreAuthorized
 *   (checks the given expression BEFORE entering the method) and @PostAuthorized (verifies given
 *   expression AFTER exiting the method) annotations
 * WebSecurityConfigurerAdapter - implements Spring Security's WebSecurityConfigurer interface. It
 * provides default security configurations and allows other classes to extend it and customize the
 * security configurations by overriding it's methods (ex: configure())
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * This required bean (implementation of UserDetailsService) authenticates User(s) and performs various
     * role-based checks, Spring Security uses this service to load User Details from the database
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    /**
     * Used to return 401 unauthorized error to clients that try to access a protected resource without proper
     * authentication. It implements Spring Security's AuthenticationEntryPoint interface
     */
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    /**
     * JwtAuthenticationFilter implements a filter to:
     * - Read JWT authentication token from the Authorization header of all requests
     * - Validates JWT token
     * - Loads the User Details associated to the token's User ID
     * - Sets the User Details in Spring Security's SecurityContext. Spring Security uses the UserDetails to
     * perform authorization checks. Can also access the UserDetails stored in the SecurityContext in the
     * controllers to perform business logic
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    // AuthenticationManager is used to authenticate a User when logging into the API
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*.gif",
                            "/**/*.svg",
                            "/**/*.jpg",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js")
                        .permitAll()
                    .antMatchers("/api/v1/auth/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/api/v1/user/checkUsernameAvailability",
                            "/api/v1/user/checkEmailAvailability").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/flights/**", "/api/v1/cities/**").permitAll()
                    .anyRequest().authenticated();
        // Needed for H2
        http.headers().frameOptions().disable();
        // Add custom JWT Security Filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
