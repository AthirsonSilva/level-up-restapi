package com.api.nextspring.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.nextspring.enums.UserRoles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("preHandle method called, for the URL: " + request.getRequestURL());

		// Check if the user has the ADMIN role
		if (!request.isUserInRole(UserRoles.ADMIN.name())) {
			log.error("User does not have the ADMIN role");

			return true;
		}

		log.info("User has the ADMIN role");

		return true;
	}
}
