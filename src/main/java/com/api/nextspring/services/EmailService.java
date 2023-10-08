package com.api.nextspring.services;

import com.api.nextspring.dto.EmailDto;

public interface EmailService {
	EmailDto sendConfirmationEmail(EmailDto email);

	EmailDto sendPasswordResetEmail(EmailDto email);

	String buildPasswordResetEmail(String name, String content);

	String buildConfirmationEmail(String name, String link);
}
