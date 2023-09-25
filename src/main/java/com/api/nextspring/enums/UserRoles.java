package com.api.nextspring.enums;

import static com.api.nextspring.enums.UserPermissions.DEVELOPER_READ;
import static com.api.nextspring.enums.UserPermissions.GAME_READ;
import static com.api.nextspring.enums.UserPermissions.GENRE_READ;
import static com.api.nextspring.enums.UserPermissions.GENRE_WRITE;
import static com.api.nextspring.enums.UserPermissions.USER_READ;
import static com.api.nextspring.enums.UserPermissions.USER_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum UserRoles {
	ADMIN(Sets.newHashSet(
			USER_READ,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ,
			GENRE_WRITE)),
	USER(Sets.newHashSet(
			USER_READ,
			USER_WRITE,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ)),
	DEVELOPER(Sets.newHashSet(
			USER_READ,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ,
			GENRE_WRITE));

	private final Set<UserPermissions> permissions;

	UserRoles(Set<UserPermissions> permissions) {
		this.permissions = permissions;
	}

	public Set<UserPermissions> getPermissions() {
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
