package com.api.levelup.utils;

import org.springframework.stereotype.Service;

import com.api.levelup.dto.Response;

/**
 * @param <K> The type of the message
 * @param <V> The type of the data
 * 
 *            This class provides a method to generate a response with a message
 *            and data
 * 
 * @see Response
 * 
 * @author Athirson Silva
 */
@Service
public class ResponseGenerator<K, V> {

	/**
	 * Generates a response with the message and data passed as parameters in the
	 * 
	 * @param message The message to be returned in the response
	 * @param data    The data to be returned in the response
	 * @return The response with the message and data passed as parameters in the
	 *         body of the response
	 */
	public Response<String, V> responseGenerator(K message, V data) {
		return new Response<>(message.toString(), data);
	}
}