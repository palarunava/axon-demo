package com.cognizant.axon.coreapi;

import javax.validation.constraints.NotNull;

import lombok.Value;

@Value
public class MoneyWithdrawnEvent {

	@NotNull
	private String accountId;
	
	@NotNull
	private int amount;
	
	@NotNull
	private int balance;
}
