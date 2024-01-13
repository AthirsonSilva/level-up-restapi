package com.api.levelup.services.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.api.levelup.dto.DeveloperDto;
import com.api.levelup.dto.GameDto;
import com.api.levelup.dto.GenreDto;
import com.api.levelup.dto.UserDto;
import com.api.levelup.services.LinkingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class implements the LinkingService interface and provides methods to
 * add HATEOAS links to DTO classes.
 * It also provides methods to get the server complete address, application
 * host, and controller request URL.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@Getter
@RequiredArgsConstructor
public class LinkingServiceImpl implements LinkingService {
	@Value("${server.address:localhost}")
	private String serverAddress;

	@Value("${server.port:8080}")
	private String serverPort;

	private final Environment environment;

	/**
	 * This method returns the complete server address.
	 *
	 * @return The complete server address.
	 */
	@Override
	public String getServerCompleteAddress() {
		String localAddress = new String();

		try {
			localAddress = InetAddress.getLocalHost().getHostAddress() + ":" + serverPort;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (localAddress.equals(""))
			localAddress = serverAddress + ":" + serverPort;

		return localAddress;
	}

	/**
	 * This method returns the application host.
	 *
	 * @return The application host.
	 */
	@Override
	public String getApplicationHost() {
		String host = new String();

		try {
			host = NetworkInterface.getNetworkInterfaces()
					.nextElement()
					.getInetAddresses()
					.nextElement()
					.getHostAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return host;
	}

	/**
	 * This method returns the request URL for the given resource.
	 *
	 * @param request  The HttpServletRequest object.
	 * @param resource The resource name.
	 * @return The request URL for the given resource.
	 */
	@Override
	public String getControllersRequestUrl(HttpServletRequest request, String resource) {
		return request.getHeader("host") + "/api/v1/" + resource;
	}

	/**
	 * This method adds HATEOAS links to the given GameDto object.
	 *
	 * @param request  The HttpServletRequest object.
	 * @param resource The resource name.
	 * @param model    The GameDto object.
	 * @return The GameDto object with HATEOAS links added.
	 */
	@Override
	public GameDto addHateoasLinksToClass(HttpServletRequest request, String resource,
			GameDto model) {
		model.add(
				List.of(
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"GET - self"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"GET - all"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/search?query=" + model.getName(),
								"GET - search"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"POST - create"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"PATCH - update"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"DELETE - delete")));

		return model;
	}

	/**
	 * This method adds HATEOAS links to the given GenreDto object.
	 *
	 * @param request  The HttpServletRequest object.
	 * @param resource The resource name.
	 * @param model    The GenreDto object.
	 * @return The GenreDto object with HATEOAS links added.
	 */
	@Override
	public GenreDto addHateoasLinksToClass(HttpServletRequest request, String resource, GenreDto model) {
		model.add(
				List.of(
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"GET - self"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"GET - all"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/search?query=" + model.getName(),
								"GET - search"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"POST - create"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"PATCH - update"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"DELETE - delete")));

		return model;
	}

	/**
	 * This method adds HATEOAS links to the given UserDto object.
	 *
	 * @param request  The HttpServletRequest object.
	 * @param resource The resource name.
	 * @param model    The UserDto object.
	 * @return The UserDto object with HATEOAS links added.
	 */
	@Override
	public UserDto addHateoasLinksToClass(HttpServletRequest request, String resource, UserDto model) {
		model.add(
				List.of(
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"GET - self"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"GET - all"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/search?query=" + model.getName(),
								"GET - search"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"POST - create"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"PATCH - update"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"DELETE - delete")));

		return model;
	}

	/**
	 * This method adds HATEOAS links to the given DeveloperDto object.
	 *
	 * @param request  The HttpServletRequest object.
	 * @param resource The resource name.
	 * @param model    The DeveloperDto object.
	 * @return The DeveloperDto object with HATEOAS links added.
	 */
	@Override
	public DeveloperDto addHateoasLinksToClass(HttpServletRequest request, String resource, DeveloperDto model) {
		model.add(
				List.of(
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"GET - self"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"GET - all"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/search?query=" + model.getName(),
								"GET - search"),
						Link.of(
								getControllersRequestUrl(request, resource),
								"POST - create"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"PATCH - update"),
						Link.of(
								getControllersRequestUrl(request, resource) + "/" + model.getId(),
								"DELETE - delete")));

		return model;
	}

}
