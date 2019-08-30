package com.cognizant.axon.account;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.cognizant.axon.coreapi.AccountCreatedEvent;
import com.cognizant.axon.coreapi.CreateAccountCommand;
import com.cognizant.axon.coreapi.MoneyWithdrawnEvent;
import com.cognizant.axon.coreapi.WithdrawMoneyCommand;

import lombok.NoArgsConstructor;

@Aggregate/*(repository="jpaAccountRepository")*/
@NoArgsConstructor
@Entity
public class Account {

	@Id
	@AggregateIdentifier
	private String accountId;
	
	@Basic
	private int balance;
	
	@Basic
	private int overdraftLimit;
	
	@CommandHandler
	public Account(CreateAccountCommand command) {
		apply(new AccountCreatedEvent(command.getAccountId(), command.getOverdraftLimit()));
	}
	
	@CommandHandler
	public void handle(WithdrawMoneyCommand command) throws OverdraftLimitExceededException {
		if (balance + overdraftLimit >= command.getAmount()) {
			apply(new MoneyWithdrawnEvent(command.getAccountId(), command.getAmount(), balance - command.getAmount()));
		} else {
			throw new OverdraftLimitExceededException();
		}
	}
	
	@EventSourcingHandler
	public void on(AccountCreatedEvent event) {
		this.accountId = event.getAccountId();
		this.overdraftLimit = event.getOverdraftLimit();
	}
	
	@EventSourcingHandler
	public void on(MoneyWithdrawnEvent event) {
		this.balance = event.getBalance();
	}
}
