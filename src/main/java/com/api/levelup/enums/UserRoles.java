package com.api.levelup.enums;

import static com.api.levelup.enums.UserPermissions.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

/**
 * Enum representing the different roles that a user can have in the system.
 * Each role has a set of permissions associated with it.
 * 
 * @param ADMIN     The admin role, with all permissions.
 * @param USER      The user role, with permissions to read and write
 *                  users,
 *                  games, genres, and developers.
 * @param DEVELOPER The developer role, with permissions to read users,
 *                  games,
 *                  genres, and developers.
 * 
 * @see {@link com.api.levelup.entity.UserEntity} the User entity class
 * @see {@link com.api.levelup.entity.UserEntity#getRoles()} the method that
 *      returns the user's roles
 * @see {@link com.api.levelup.security.SecurityManagement} the class that
 *      configures the security of the application
 * @see {@link UserPermissions} the enum representing the permissions that a
 *      user
 *      can have in the system
 * @see {@link UserPermissions#getPermission()} the method that returns the
 *      permission string value
 * 
 * @author Athirson Silva
 */
public enum UserRoles {

	ADMIN(Sets.newHashSet(
			USER_READ,
			USER_WRITE,
			GAME_READ,
			GAME_WRITE,
			DEVELOPER_READ,
			DEVELOPER_WRITE,
			GENRE_READ,
			GENRE_WRITE)),

	USER(Sets.newHashSet(
			USER_READ,
			USER_WRITE,
			GAME_READ,
			DEVELOPER_READ,
			GENRE_READ));

	private final Set<UserPermissions> permissions;

	UserRoles(Set<UserPermissions> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Returns the set of permissions associated with this role.
	 *
	 * @return the set of permissions associated with this role
	 */
	public Set<UserPermissions> getPermissions() {
		return permissions;
	}

	/**
	 * Returns the set of granted authorities associated with this role.
	 *
	 * @return the set of granted authorities associated with this role
	 */
	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
		Set<SimpleGrantedAuthority> permissions = getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());

		permissions.add(new SimpleGrantedAuthority(this.name()));

		return permissions;
	}
}
