package com.api.nextspring.services;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

/**
 * This interface defines methods for retrieving server and application
 * information, constructing request URLs, and adding HATEOAS links to DTOs.
 * 
 * @author Athirson Silva
 * @see com.api.nextspring.services.impl.LinkingServiceImpl
 */
public interface LinkingService {

	/**
	 * Returns the complete address of the server.
	 * 
	 * @return the complete address of the server
	 */
	String getServerCompleteAddress();

	/**
	 * Returns the host of the application.
	 * 
	 * @return the host of the application
	 */
	String getApplicationHost();

	/**
	 * Constructs a request URL for a given resource and HTTP request.
	 * 
	 * @param request  the HTTP request
	 * @param resource the resource to construct the URL for
	 * @return the constructed request URL
	 */
	String getControllersRequestUrl(HttpServletRequest request, String resource);

	/**
	 * Adds HATEOAS links to a GameDto object.
	 * 
	 * @param request  the HTTP request
	 * @param resource the resource to construct the links for
	 * @param model    the GameDto object to add the links to
	 * @return the GameDto object with added HATEOAS links
	 */
	GameDto addHateoasLinksToClass(HttpServletRequest request, String resource, GameDto model);

	/**
	 * Adds HATEOAS links to a GenreDto object.
	 * 
	 * @param request  the HTTP request
	 * @param resource the resource to construct the links for
	 * @param model    the GenreDto object to add the links to
	 * @return the GenreDto object with added HATEOAS links
	 */
	GenreDto addHateoasLinksToClass(HttpServletRequest request, String resource, GenreDto model);

	/**
	 * Adds HATEOAS links to a UserDto object.
	 * 
	 * @param request  the HTTP request
	 * @param resource the resource to construct the links for
	 * @param model    the UserDto object to add the links to
	 * @return the UserDto object with added HATEOAS links
	 */
	UserDto addHateoasLinksToClass(HttpServletRequest request, String resource, UserDto model);

	/**
	 * Adds HATEOAS links to a DeveloperDto object.
	 * 
	 * @param request  the HTTP request
	 * @param resource the resource to construct the links for
	 * @param model    the DeveloperDto object to add the links to
	 * @return the DeveloperDto object with added HATEOAS links
	 */
	DeveloperDto addHateoasLinksToClass(HttpServletRequest request, String resource, DeveloperDto model);
}
