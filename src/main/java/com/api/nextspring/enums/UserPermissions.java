package com.api.nextspring.enums;

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

	public String getPermission() {
		return permission;
	}
}
