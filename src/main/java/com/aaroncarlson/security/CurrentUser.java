package com.aaroncarlson.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Spring Security provides an annotation called @AuthenticationPrincipal to access the currently
 * authenticated User in the controllers.
 *
 * In addition created a meta-annotation so that the project doesn't get too tied up with Spring
 * Security related annotations throughout the project. THis reduces the dependency on Spring
 * Security. If the logic is changed to remove Spring Security from the project, it can easily be
 * done by chaning this annotation class CurrentUser.
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
