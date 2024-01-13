package com.api.levelup.enums;

import com.api.levelup.entity.DeveloperEntity;
import com.api.levelup.entity.GameEntity;
import com.api.levelup.entity.GenreEntity;
import com.api.levelup.entity.UserEntity;

/**
 * This enum represents the different entity options available in the
 * application.
 * It includes options for Game, Genre, Developer, and User.
 * 
 * @see {@link GameEntity} the Game entity class, representing the GAME enum
 * @see {@link GenreEntity} the Genre entity class, representing the GENRE enum
 * @see {@link DeveloperEntity} the Developer entity class, representing the
 *      DEVELOPER enum
 * @see {@link UserEntity} the User entity class, representing the USER enum
 * 
 * @param value The string value of the enum.
 * 
 * @author Athirson Silva
 */
public enum EntityOptions {
	GAME("Game"),
	GENRE("Genre"),
	DEVELOPER("Developer"),
	USER("User");

	private String value;

	private EntityOptions(String value) {
		this.value = value;
	}

	/**
	 * Returns the string value of the enum.
	 * 
	 * @return the string value of the enum
	 */
	public String getValue() {
		return value;
	}
}
