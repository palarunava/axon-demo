package com.cognizant.axon.coreapi;

import javax.validation.constraints.NotNull;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class WithdrawMoneyCommand {

	@NotNull
	@TargetAggregateIdentifier
	private String accountId;
	
	@NotNull
	private int amount;
}
