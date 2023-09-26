package com.api.nextspring.services.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.UserDto;
import com.api.nextspring.services.LinkingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class LinkingServiceImpl implements LinkingService {
	@Value("${server.address:localhost}")
	private String serverAddress;

	@Value("${server.port:8000}")
	private String serverPort;

	private final Environment environment;

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

	public String getControllersRequestUrl(HttpServletRequest request, String resource) {
		return request.getHeader("host") + "/api/v1/" + resource;
	}

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
