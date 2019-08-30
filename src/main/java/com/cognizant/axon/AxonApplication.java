package com.cognizant.axon;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.cognizant.axon.coreapi.CreateAccountCommand;
import com.cognizant.axon.coreapi.WithdrawMoneyCommand;

@SpringBootApplication
public class AxonApplication {
	
	@Autowired
	private TransactionManager transactionManager;

	@PersistenceContext
	private EntityManager entityManager;
	
    public static void main(String[] args) {
		final ConfigurableApplicationContext configuration = SpringApplication.run(AxonApplication.class, args);
		final CommandBus commandBus = configuration.getBean(CommandBus.class);
		/*commandBus.dispatch(asCommandMessage(new CreateAccountCommand("4321", 500)));
		commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 250)));
		commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 251)));*/
		commandBus.dispatch(asCommandMessage(new CreateAccountCommand("4321", 500)), new CommandCallback<CreateAccountCommand, Object>() {

			@Override
			public void onFailure(CommandMessage<? extends CreateAccountCommand> command, Throwable cause) {
				cause.printStackTrace();
			}

			@Override
			public void onSuccess(CommandMessage<? extends CreateAccountCommand> command, Object payload) {
				System.out.println(payload);
				commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 250)));
				commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 251)));
			}
		});
	}
	
	/*@Bean
	public EntityManagerProvider entityManagerProvider() {
		return new ContainerManagedEntityManagerProvider();
	}*/
	
	/*@Bean
	public Repository<Account> jpaAccountRepository(EventBus eventBus) {
		return new GenericJpaRepository<>(entityManagerProvider(), Account.class, eventBus);
	}*/
	
	/*@Bean
	public TransactionManager axonTransactionManager(PlatformTransactionManager transactionManager) {
		return new SpringTransactionManager(transactionManager);
	}*/
	
	/*@Bean
	public EventStorageEngine eventStorageEngine() {
		return new InMemoryEventStorageEngine();
	}*/
	
	/*@Bean
	public JpaEventStorageEngine eventStorageEngine() {
		return new JpaEventStorageEngine(entityManagerProvider(), transactionManager);
	}*/
	
	@Bean
	public CommandBus commandBus() {
		final AsynchronousCommandBus commandBus = new AsynchronousCommandBus();
		commandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());
		commandBus.registerHandlerInterceptor(new TransactionManagingInterceptor<>(transactionManager));
		return commandBus;
	}
}
