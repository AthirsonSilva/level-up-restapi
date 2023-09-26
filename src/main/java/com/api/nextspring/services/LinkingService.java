package com.api.nextspring.services;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

public interface LinkingService {
	String getServerCompleteAddress();

	String getApplicationHost();

	String getControllersRequestUrl(HttpServletRequest request, String resource);

	GameDto addHateoasLinksToClass(HttpServletRequest request, String resource, GameDto model);

	GenreDto addHateoasLinksToClass(HttpServletRequest request, String resource, GenreDto model);

	UserDto addHateoasLinksToClass(HttpServletRequest request, String resource, UserDto model);

	DeveloperDto addHateoasLinksToClass(HttpServletRequest request, String resource, DeveloperDto model);
}
