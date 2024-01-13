package com.api.levelup.services;

import com.api.levelup.dto.GameBuyingResponse;
import com.api.levelup.dto.StripeTokenResponse;
import com.api.levelup.dto.request.GameBuyingRequest;
import com.api.levelup.dto.request.StripeChargeRequest;

/**
 * This interface provides methods for creating a Stripe card token and charging
 * a Stripe account.
 * 
 * @see <a href="https://stripe.com/docs/api?lang=java">Stripe Java API library
 *      reference</a>
 * 
 * @author Athirson Silva
 */
public interface StripeService {

	/**
	 * Creates a Stripe card token using the provided StripeTokenDto.
	 * 
	 * @param tokenDto The StripeTokenDto containing the card information.
	 * @return The StripeTokenDto containing the token information.
	 */
	StripeTokenResponse createCardToken(GameBuyingRequest tokenDto);

	/**
	 * Charges a Stripe account using the provided StripeChargeDto.
	 * 
	 * @param chargeRequest The StripeChargeDto containing the charge information.
	 * @return The StripeChargeDto containing the charge result information.
	 */
	GameBuyingResponse createCharge(StripeChargeRequest chargeRequest);
}
