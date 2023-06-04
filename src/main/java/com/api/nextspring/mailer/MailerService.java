package com.api.nextspring.mailer;

import com.api.nextspring.payload.EmailDto;

public interface MailerService {
	EmailDto sendEmail(EmailDto email);

	String buildEmail(String name, String link);
}
