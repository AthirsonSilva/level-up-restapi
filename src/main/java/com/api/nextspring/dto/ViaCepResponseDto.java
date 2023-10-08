package com.api.nextspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViaCepResponseDto {
	private String cep;

	private String logradouro;

	private String complemento;

	private String bairro;

	private String localidade;

	private String uf;

	private String ibge;

	private String gia;

	private String ddd;

	private String siafi;
}
