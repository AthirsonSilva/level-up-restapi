package com.api.nextspring.enums;

import com.api.nextspring.entity.GameEntity;

/**
 * An enumeration representing the different rating options for a game.
 * The options are AAA, AA, A, B, and C.
 * 
 * @param AAA The AAA rating option, the highest rating.
 * @param AA  The AA rating option, the second highest rating.
 * @param A   The A rating option, the third highest rating.
 * @param B   The B rating option, the fourth highest rating.
 * @param C   The C rating option, the fifth highest rating.
 * 
 * @see {@link GameEntity} the Game entity class, representing the GAME enum
 * @see {@link GameEntity#getRating() getRating()} the getRating() method in the
 * 
 * @author Athirson Silva
 */
public enum GameRatingOptions {
	AAA("aaa"),
	AA("aa"),
	A("a"),
	B("b"),
	C("c");

	private String rating;

	private GameRatingOptions(String rating) {
		this.rating = rating;
	}

	/**
	 * Returns the string value of the enum.
	 * 
	 * @return the string value of the enum
	 */
	public String getRating() {
		return rating;
	}
}
