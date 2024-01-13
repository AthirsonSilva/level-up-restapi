package com.api.levelup.enums;

/**
 * This enum represents the permissions that a user can have in the system.
 * Each permission is represented by a string value that can be used to check if
 * a user has that permission.
 * 
 * @param USER_READ       The permission to read users.
 * @param USER_WRITE      The permission to write users.
 * @param GAME_READ       The permission to read games.
 * @param GAME_WRITE      The permission to write games.
 * @param GENRE_READ      The permission to read genres.
 * @param GENRE_WRITE     The permission to write genres.
 * @param DEVELOPER_READ  The permission to read developers.
 * @param DEVELOPER_WRITE The permission to write developers.
 * 
 * @see {@link com.api.levelup.entity.UserEntity} the User entity class
 * @see {@link com.api.levelup.entity.UserEntity#getPermissions()} the method
 *      that returns the user's permissions
 * @see {@link com.api.levelup.security.SecurityManagement} the class that
 *      configures the security of the application
 * 
 * @author Athirson Silva
 */
public enum UserPermissions {
	USER_READ("user:read"),
	USER_WRITE("user:write"),
	GAME_READ("game:read"),
	GAME_WRITE("game:write"),
	GENRE_READ("genre:read"),
	GENRE_WRITE("genre:write"),
	DEVELOPER_READ("developer:read"),
	DEVELOPER_WRITE("developer:write");

	private final String permission;

	UserPermissions(String permission) {
		this.permission = permission;
	}

	/**
	 * Returns the string value of the enum.
	 * 
	 * @return the string value of the enum
	 */
	public String getPermission() {
		return permission;
	}
}
