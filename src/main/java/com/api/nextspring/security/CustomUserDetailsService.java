package com.api.nextspring.security;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.ApplicationUserRoles;
import com.api.nextspring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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

		// return a new user with the username, password, and authorities
		return User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles(user.getRoles().stream().map(RoleEntity::getName).toArray(String[]::new))
				.authorities(ApplicationUserRoles.USER.getGrantedAuthorities())
				.build();
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<RoleEntity> roles) {
		// convert the roles to a list of authorities
		return roles.stream().map(
				role -> new SimpleGrantedAuthority(role.getName())
		).collect(Collectors.toList());
	}
}
