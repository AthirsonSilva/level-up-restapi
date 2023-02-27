package com.api.nextspring.utils;

import com.api.nextspring.payload.Response;
import org.springframework.stereotype.Service;


@Service
public class GenerateHashMapResponse<K, V> {
	public Response<K, V> generate(K message, V data) {
		return new Response<>(message, data);
	}
}