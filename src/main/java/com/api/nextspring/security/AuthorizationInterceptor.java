package com.api.nextspring.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.enums.UserRoles;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.JwtTokenExtractor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class is responsible for intercepting incoming HTTP requests and
 * checking if the user has the necessary
 * authorization to access the requested resource. It implements the
 * HandlerInterceptor interface and overrides the
 * preHandle method to perform the authorization check. If the user is not
 * authorized, a RestApiException with a
 * FORBIDDEN status code is thrown.
 *
 * The class uses a UserService and a JwtTokenExtractor to retrieve the user's
 * role and JWT token, respectively.
 * If the user's role is not ADMIN, a RestApiException with a FORBIDDEN status
 * code is thrown.
 * 
 * @see HandlerInterceptor
 * @see UserService
 * @see JwtTokenExtractor
 * @see RestApiException
 * @see RoleEntity
 * @see UserRoles
 * 
 * @author Athirson Silva
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

	private final UserService userServices;
	private final JwtTokenExtractor tokenExtractor;

	/**
	 * This method is called before the request is handled by the controller. It
	 * checks if the user has the necessary permissions to access the requested
	 * resource.
	 * 
	 * @param request  The incoming HTTP request.
	 * @param response The HTTP response.
	 * @param handler  The handler for the request.
	 * @return Returns true if the user has the necessary permissions to access the
	 *         requested resource, false otherwise.
	 * @throws Exception Throws an exception if there was an error while processing
	 *                   the request.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURL = request.getRequestURL().toString();

		log.info("preHandle method called, for the URL: " + requestURL);

		Map<String, String> headers = new HashMap<>();
		headers.put("authorization", request.getHeader("authorization"));

		log.info(
				headers.get("authorization").isEmpty() ? "Did not receive JWT token on headers"
						: "Received JWT token on headers");

		RoleEntity role = getRole(headers);

		log.info("Role: " + role);

		if (!role.getName().equalsIgnoreCase(UserRoles.ADMIN.toString()))
			throw new RestApiException(HttpStatus.FORBIDDEN, "User does not have access permission: " + role.getName());

		return true;
	}

	/**
	 * This method extracts the JWT token from the request headers and retrieves the
	 * user's role from the database.
	 * 
	 * @param headers The HTTP headers from the incoming request.
	 * @return Returns the user's role.
	 */
	private RoleEntity getRole(Map<String, String> headers) {
		String token = tokenExtractor.execute(headers);

		Set<RoleEntity> userRoles = userServices.getUserRole(token);

		log.info("User roles: " + userRoles);

		RoleEntity role = userRoles.stream().findFirst()
				.orElseThrow(() -> new RestApiException(HttpStatus.UNAUTHORIZED, "User is not logged in"));

		if (!role.getName().equals("ADMIN"))
			throw new RestApiException(HttpStatus.FORBIDDEN, "User does not have access permission: " + role.getName());

		return role;
	}
}
