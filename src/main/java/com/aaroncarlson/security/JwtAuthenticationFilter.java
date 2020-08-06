package com.aaroncarlson.security;

import com.aaroncarlson.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Goal: Get JWT token from request, validate it and then load the User associated with the token, then finally pass
 * authentication token to Spring Security.
 * 1. Parse and read JWT retrieved from Authorization header of the request and obtain the User's ID (IF JWT is valid)
 * 2. Load persisted User from database based on User ID
 * 3. Set Username and Password Authentication token based on User Details (aka: CustomUserDetailsService)
 * 4. Set authentication inside Spring Security context. Spring Security uses the user details to perform authorization
 *    checks. Can also access user details stored in SecurityContext in controllers to perform business logic
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromJwt(jwt);

                /*
                    NOTE: Can encode User's username and roles inside the JWT claims and then create the
                    UserDetails object by parsing those values from the JWT claims. This would avoid the
                    extra database hit. However, loading the current details of the User, from the database,
                    might still be helpful (ex: may want to disallow login with this JWT if User's role
                    changes, or the User updates their password after creation of the current JWT token).
                 */
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            logger.error("Could not set User authentication security context", exception);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AppConstants.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(AppConstants.BEARER + AppConstants.SPACE)) {
            return authorizationHeader.substring(7, authorizationHeader.length());
        }
        return null;
    }

}
