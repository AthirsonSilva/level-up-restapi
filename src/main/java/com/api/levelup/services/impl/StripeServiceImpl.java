package com.api.levelup.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.levelup.dto.GameBuyingResponse;
import com.api.levelup.dto.StripeTokenResponse;
import com.api.levelup.dto.request.GameBuyingRequest;
import com.api.levelup.dto.request.StripeChargeRequest;
import com.api.levelup.exceptions.RestApiException;
import com.api.levelup.services.StripeService;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.TokenCreateParams;
import com.stripe.param.TokenCreateParams.Card;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Implementation of the StripeService interface that provides methods for
 * creating a card token and charging a card using Stripe API.
 * This class uses the Stripe Java library to interact with the Stripe API.
 * 
 * @see <a href="https://stripe.com/docs/api?lang=java">Stripe Java API library
 *      reference</a>
 * 
 * @implNote This class implements the StripeService interface.
 * 
 * @see com.api.levelup.services.StripeService
 * 
 * @author Athirson Silva
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

	@Value("${stripe.api.key.secret}")
	private String secretApiKey;

	@Value("${stripe.api.key.publish}")
	private String publishApiKey;

	private final ModelMapper modelMapper;

	/**
	 * Creates a Stripe card token from the given StripeTokenDto.
	 * 
	 * @param tokenRequest the StripeTokenDto containing the card information
	 * @return the StripeTokenDto with the token ID and success status
	 * @throws RestApiException if there is an error creating the token
	 */
	@Override
	public StripeTokenResponse createCardToken(GameBuyingRequest tokenRequest) {
		try {
			StripeTokenResponse tokenResponse = modelMapper.map(tokenRequest, StripeTokenResponse.class);

			Card cardParams = Card.builder()
					.setNumber(tokenRequest.getCardNumber())
					.setExpMonth(tokenRequest.getExpirationMonth())
					.setExpYear(tokenRequest.getExpirationYear())
					.setCvc(tokenRequest.getCvc())
					.setCurrency("BRL")
					.setName(tokenRequest.getUsername())
					.build();

			TokenCreateParams tokenParams = TokenCreateParams
					.builder()
					.setCard(cardParams)
					.build();

			RequestOptions requestOptions = RequestOptions.builder().setApiKey(publishApiKey).build();

			Token token = Token.create(tokenParams, requestOptions);

			if (token != null && token.getId() != null) {
				tokenResponse.setSuccess(true);
				tokenResponse.setToken(token.getId());
			}

			return tokenResponse;
		} catch (Exception e) {
			log.error("Error creating card token: " + e.getMessage());

			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating card token");
		}
	}

	/**
	 * Charges the given amount to the given StripeChargeDto.
	 * 
	 * @param chargeRequest the StripeChargeDto containing the charge information
	 * @return the StripeChargeDto with the charge ID and success status
	 * @throws RestApiException if there is an error charging the card
	 */
	@Override
	public GameBuyingResponse createCharge(StripeChargeRequest chargeRequest) {
		try {
			GameBuyingResponse chargeResponse = modelMapper.map(chargeRequest, GameBuyingResponse.class);

			chargeResponse.setSuccess(false);

			ChargeCreateParams chargeParams = ChargeCreateParams
					.builder()
					.setAmount((long) (chargeRequest.getAmount() * 100))
					.setCurrency("BRL")
					.setDescription("Payment for: " + chargeRequest.getUsername())
					.setSource(chargeRequest.getToken())
					.setMetadata(chargeRequest.getMetadata())
					.build();

			RequestOptions requestOptions = RequestOptions.builder().setApiKey(secretApiKey).build();

			Charge charge = Charge.create(chargeParams, requestOptions);

			if (charge.getPaid()) {
				chargeResponse.setSuccess(true);
				chargeResponse.setMessage("The card was charged successfully!");
				chargeResponse.setChargeId(charge.getId());
			}

			return chargeResponse;
		} catch (Exception e) {
			log.error("Error charging card: " + e.getMessage());

			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error charging card");
		}
	}
}
