package com.api.nextspring.utils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Service
public class GetJwtTokenFromHeaders {
	public String execute(@RequestHeader Map<String, String> headers) {
		String bearerToken = headers.get("authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return bearerToken;
	}
}
