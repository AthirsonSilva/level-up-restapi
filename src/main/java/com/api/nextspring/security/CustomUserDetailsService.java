package com.api.nextspring.security;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.ApplicationUserRoles;
import com.api.nextspring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	/**
	 * load user by username
	 *
	 * @param email the user email
	 * @return {@link UserDetails}
	 * @throws UsernameNotFoundException org.springframework.security.core.userdetails. username not found exception
	 * @see UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Let people login with either username or email
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() ->
						new UsernameNotFoundException("User not found with username or email:" + email));

		// get the user permissions based on the user roles
		Set<SimpleGrantedAuthority> grantedAuthorities = ApplicationUserRoles.valueOf(
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
