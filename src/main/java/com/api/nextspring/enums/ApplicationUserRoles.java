package com.api.nextspring.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.api.nextspring.enums.ApplicationUserPermissions.*;

public enum ApplicationUserRoles {
	ADMIN(Sets.newHashSet(
			USER_READ,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ,
			GENRE_WRITE
	)),
	USER(Sets.newHashSet(
			USER_READ,
			USER_WRITE,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ
	)),
	DEVELOPER(Sets.newHashSet(
			USER_READ,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ,
			GENRE_WRITE
	));

	private final Set<ApplicationUserPermissions> permissions;

	ApplicationUserRoles(Set<ApplicationUserPermissions> permissions) {
		this.permissions = permissions;
	}

	public Set<ApplicationUserPermissions> getPermissions() {
		return permissions;
	}

	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
		Set<SimpleGrantedAuthority> permissions = getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());

		permissions.add(new SimpleGrantedAuthority(this.name()));

		return permissions;
	}
}
