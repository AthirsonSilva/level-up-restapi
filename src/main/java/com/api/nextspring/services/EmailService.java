package com.api.nextspring.services;

import com.api.nextspring.dto.EmailDto;

public interface EmailService {
	EmailDto sendEmail(EmailDto email);

	String buildEmail(String name, String link);
}
