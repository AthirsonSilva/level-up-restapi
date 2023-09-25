package com.api.nextspring.mailer;

import com.api.nextspring.dto.EmailDto;

public interface MailerService {
	EmailDto sendEmail(EmailDto email);

	String buildEmail(String name, String link);
}
