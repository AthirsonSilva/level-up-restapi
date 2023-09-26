package com.api.nextspring.utils;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class JwtTokenUtils {
	public String execute(@RequestHeader Map<String, String> headers) {
		String bearerToken = headers.get("authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return bearerToken;
	}
}
