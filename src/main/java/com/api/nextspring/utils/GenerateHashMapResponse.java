package com.api.nextspring.utils;

import org.springframework.stereotype.Service;

import com.api.nextspring.dto.Response;

/**
 * @param <K> The type of the message
 * @param <V> The type of the data
 */
@Service
public class GenerateHashMapResponse<K, V> {
	/**
	 * @param message The message to be returned
	 * @param data    The data to be returned
	 * @return The response with the message and data passed as parameters
	 */
	public Response<String, V> generateHashMapResponse(K message, V data) {
		return new Response<>(message.toString(), data);
	}
}