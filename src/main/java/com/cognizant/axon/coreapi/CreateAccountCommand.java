package com.cognizant.axon.coreapi;

import javax.validation.constraints.NotNull;

import lombok.Value;

@Value
public class CreateAccountCommand {

	@NotNull
	private String accountId;
	
	@NotNull
	private int overdraftLimit;
}
