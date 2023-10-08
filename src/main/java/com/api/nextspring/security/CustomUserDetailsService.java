package com.api.nextspring.security;

import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.UserRoles;
import com.api.nextspring.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * This class implements the UserDetailsService interface and provides a custom implementation for loading user details.
 * It loads user details based on the email provided and returns a UserDetails object containing the user's email, password, roles, and authorities.
 * The user details are retrieved from the UserRepository.
 */
/**
 * This method loads a user by their email address and returns a UserDetails
 * object.
 *
 * @param email the email address of the user to load
 * @return a UserDetails object representing the loaded user
 * @throws UsernameNotFoundException if no user is found with the given email
 *                                   address
 * @see UserDetails
 * @author Athirson Silva
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	/**
	 * load user by username
	 *
	 * @param email the user email
	 * @return {@link UserDetails}
	 * @throws UsernameNotFoundException org.springframework.security.core.userdetails.
	 *                                   username not found exception
	 * @see UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Let people login with either username or email
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username or email:" + email));

		// get the user permissions based on the user roles
		Set<SimpleGrantedAuthority> grantedAuthorities = UserRoles.valueOf(
				user.getRoles()
						.stream()
						.map(RoleEntity::getName)
						.findFirst()
						.orElse(null))
				.getGrantedAuthorities();

		// convert the user roles to a string array
		String[] userRoles = user.getRoles().stream().map(RoleEntity::getName).toArray(String[]::new);

		// return a new user with the username, password, and authorities
		return User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles(userRoles)
				.authorities(grantedAuthorities)
				.build();
	}
}
