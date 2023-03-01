package com.api.nextspring.payload;

import lombok.Data;

@Data
public class OptionalUserDto {
	private String name;
	private String email;
	private String cpf;
	private String password;
}
