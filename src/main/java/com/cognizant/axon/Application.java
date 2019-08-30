package com.cognizant.axon;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import com.cognizant.axon.account.Account;
import com.cognizant.axon.coreapi.CreateAccountCommand;
import com.cognizant.axon.coreapi.WithdrawMoneyCommand;

public class Application {

	public static void main(String[] args) {
		Configuration configuration = DefaultConfigurer.defaultConfiguration()
				.configureAggregate(Account.class)
				/*.configureEmbeddedEventStore(new Function<Configuration, EventStorageEngine>() {					
					@Override
					public EventStorageEngine apply(Configuration t) {
						return new InMemoryEventStorageEngine();
					}
				})*/
				.configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
				//.configureCommandBus(c -> new AsynchronousCommandBus())
				.buildConfiguration();
		
		configuration.start();
		configuration.commandBus().dispatch(asCommandMessage(new CreateAccountCommand("4321", 500)));
		configuration.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 250)));
		configuration.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 251)));
	}
}
