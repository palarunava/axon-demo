package com.cognizant.axon.account;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.cognizant.axon.coreapi.AccountCreatedEvent;
import com.cognizant.axon.coreapi.CreateAccountCommand;
import com.cognizant.axon.coreapi.MoneyWithdrawnEvent;
import com.cognizant.axon.coreapi.WithdrawMoneyCommand;

public class AccountTest {
	
	private FixtureConfiguration<Account> fixture;

	@Before
	public void setup() {
		fixture = new AggregateTestFixture<>(Account.class);
	}
	
	@Test
	public void testCreateAccount() {
		fixture.givenNoPriorActivity()
			.when(new CreateAccountCommand("1234", 1000))
			.expectEvents(new AccountCreatedEvent("1234", 1000));
	}
	
	@Test
	public void testWithdrawReasonableAmount() throws Exception {
		fixture.given(new AccountCreatedEvent("1234", 1000))
			.when(new WithdrawMoneyCommand("1234", 600))
			.expectEvents(new MoneyWithdrawnEvent("1234", 600, -600));
	}
	
	@Test
	public void testWithdrawAbsurdAmount() throws Exception {
		fixture.given(new AccountCreatedEvent("1234", 1000))
			.when(new WithdrawMoneyCommand("1234", 1001))
			.expectNoEvents()
			.expectException(OverdraftLimitExceededException.class);
	}
	
	@Test
	public void testWithdrawTwice() throws Exception {
		fixture.given(new AccountCreatedEvent("1234", 1000),
				new MoneyWithdrawnEvent("1234", 999, -999))
			.when(new WithdrawMoneyCommand("1234", 2))
			.expectNoEvents()
			.expectException(OverdraftLimitExceededException.class);
	}
}
