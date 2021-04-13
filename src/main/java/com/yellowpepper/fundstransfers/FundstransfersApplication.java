package com.yellowpepper.fundstransfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.yellowpepper.fundstransfers.dto.AccountDTO;
import com.yellowpepper.fundstransfers.model.Config;
import com.yellowpepper.fundstransfers.model.Currency;
import com.yellowpepper.fundstransfers.service.AccountService;
import com.yellowpepper.fundstransfers.service.ConfigService;
import com.yellowpepper.fundstransfers.service.CurrencyService;


@SpringBootApplication
public class FundstransfersApplication {

	public static void main(final String[] args) {
		final ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(FundstransfersApplication.class, args);


		// init max daily transfer limit
		final ConfigService configService = configurableApplicationContext.getBean(ConfigService.class);
		final Config config = new Config();
		config.setCode(Config.MAX_DAILY_TRANSFERS_CONFIG_CODE);
		config.setValue("3");
		configService.save(config);

		// init currency USD
		final CurrencyService currencyService = configurableApplicationContext.getBean(CurrencyService.class);
		final Currency currency = new Currency();
		currency.setCode(Currency.CODE_CURRENCY_USD);
		currencyService.save(currency);


		// init examples accounts
		final AccountService accountService = configurableApplicationContext.getBean(AccountService.class);

		final AccountDTO account1 = new AccountDTO();
		account1.setNumber("12345600");
		account1.setBalance(70000.00);
		account1.setCurrency(Currency.CODE_CURRENCY_USD);
		accountService.save(account1);

		final AccountDTO account2 = new AccountDTO();
		account2.setNumber("12345601");
		account2.setBalance(50000.00);
		account2.setCurrency(Currency.CODE_CURRENCY_USD);
		accountService.save(account2);
	}

}
