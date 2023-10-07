package com.api.nextspring.enums;

public enum EntityOptions {
	GAME("Game"),
	GENRE("Genre"),
	DEVELOPER("Developer"),
	USER("User");

	private String value;

	private EntityOptions(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
