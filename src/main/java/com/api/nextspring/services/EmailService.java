package com.api.nextspring.services;

import com.api.nextspring.dto.EmailDto;

/**
 * This interface defines the methods for sending confirmation and password
 * reset emails. It also defines methods for building the content of these
 * emails.
 * 
 * @author Athirson Silva
 * @see com.api.nextspring.services.impl.EmailServiceImpl
 */
public interface EmailService {

	/**
	 * Sends a confirmation email.
	 * 
	 * @param email The email to be sent.
	 * @return The email that was sent.
	 */
	EmailDto sendConfirmationEmail(EmailDto email);

	/**
	 * Sends a password reset email.
	 * 
	 * @param email The email to be sent.
	 * @return The email that was sent.
	 */
	EmailDto sendPasswordResetEmail(EmailDto email);

	/**
	 * Builds the content for a password reset email.
	 * 
	 * @param name    The name of the recipient.
	 * @param content The content of the email.
	 * @return The built email content.
	 */
	String buildPasswordResetEmail(String name, String content);

	/**
	 * Builds the content for a confirmation email.
	 * 
	 * @param name The name of the recipient.
	 * @param link The confirmation link.
	 * @return The built email content.
	 */
	String buildConfirmationEmail(String name, String link);
}
