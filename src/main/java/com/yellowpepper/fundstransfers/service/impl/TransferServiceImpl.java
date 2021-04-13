package com.yellowpepper.fundstransfers.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yellowpepper.fundstransfers.dto.ErrorType;
import com.yellowpepper.fundstransfers.dto.TransferInputDTO;
import com.yellowpepper.fundstransfers.dto.TransferOutputDTO;
import com.yellowpepper.fundstransfers.model.Account;
import com.yellowpepper.fundstransfers.model.Config;
import com.yellowpepper.fundstransfers.model.Currency;
import com.yellowpepper.fundstransfers.model.Transfer;
import com.yellowpepper.fundstransfers.repository.TransferRepository;
import com.yellowpepper.fundstransfers.service.AccountService;
import com.yellowpepper.fundstransfers.service.ConfigService;
import com.yellowpepper.fundstransfers.service.CurrencyService;
import com.yellowpepper.fundstransfers.service.TransferService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransferRepository transferRepository;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CurrencyService currencyService;

	@Override
	public TransferOutputDTO send(final TransferInputDTO transferIn) throws SystemException
	{
		if (transferIn == null) {
			throw new IllegalArgumentException("Transfer cannot be null.");
		}
		final Transfer transfer = new Transfer();
		transfer.setAmount(transferIn.getAmount());
		transfer.setDescription(transferIn.getDescription());
		transfer.setDate(new Date());

		final Optional<Currency> optTransferCurrency = this.currencyService.getByCode(transferIn.getCurrency());
		if (optTransferCurrency.isEmpty()) {
			throw new IllegalArgumentException("The currency " + transferIn.getCurrency() + " isn't suppored.");
		}
		transfer.setCurrency(optTransferCurrency.get());

		final Optional<Account> optOriginAccount = this.accountService.findByNumber(transferIn.getOriginAccount());
		if (optOriginAccount.isEmpty()) {
			throw new IllegalArgumentException("Origin account does not exist.");
		}
		final Account originAccount = optOriginAccount.get();
		transfer.setOriginAccount(originAccount);

		final Optional<Account> optDestinationAccount = this.accountService.findByNumber(transferIn.getDestinationAccount());
		if (optDestinationAccount.isEmpty()) {
			throw new IllegalArgumentException("Destination account does not exist.");
		}
		final Account destinationAccount = optDestinationAccount.get();
		transfer.setDestinationAccount(destinationAccount);

		this.validateMaxDailyTransfers(originAccount);
		this.validateFundsOriginAccount(transfer);

		return this.processTransfer(transfer);
	}

	private TransferOutputDTO processTransfer(final Transfer transfer) throws SystemException {
		final TransferOutputDTO transferOut = new TransferOutputDTO();

		final Map<String, Double> exchangeRates = this.currencyService.getExchangeRates();

		final Account originAccount = transfer.getOriginAccount();
		final Double amountToOriginAccount = this.transformCurrencyValue(transfer.getCurrency().getCode(), originAccount.getCurrency().getCode(),
				transfer.getAmount(), exchangeRates);
		final Double taxToTransfer = this.calculateTax(transfer, exchangeRates);
		final Double taxToOriginAccount = this.transformCurrencyValue(transfer.getCurrency().getCode(), originAccount.getCurrency().getCode(),
				taxToTransfer, exchangeRates);
		originAccount.setBalance(originAccount.getBalance() - amountToOriginAccount - taxToOriginAccount);

		final Double taxToCAD = this.transformCurrencyValue(transfer.getCurrency().getCode(), Currency.CODE_CURRENCY_CAD,
				taxToTransfer, exchangeRates);
		transferOut.setTaxCollected(taxToTransfer);
		transferOut.setCad(taxToCAD);

		final Account destinationAccount = transfer.getDestinationAccount();
		final Double amountToDestinationAccount = this.transformCurrencyValue(transfer.getCurrency().getCode(), destinationAccount.getCurrency().getCode(),
				transfer.getAmount(), exchangeRates);
		destinationAccount.setBalance(destinationAccount.getBalance() + amountToDestinationAccount);

		this.accountService.save(originAccount);
		this.accountService.save(destinationAccount);
		this.transferRepository.save(transfer);

		return transferOut;
	}


	private Double transformCurrencyValue(final String from, final String to, final Double value, final Map<String, Double> exchangeRates) {
		Double finalValue = value;
		if (!Currency.CODE_BASE_CURRENCY.equals(from)) {
			final Double exchangeRate = exchangeRates.get(from);
			finalValue = value / exchangeRate;
		}

		Double exchangeRateTo = 1D;
		if (!Currency.CODE_BASE_CURRENCY.equals(to)) {
			exchangeRateTo = exchangeRates.get(to);
		}

		return finalValue * exchangeRateTo;
	}

	private Double calculateTax(final Transfer transfer, final Map<String, Double> exchangeRates) {
		final Double limitAmount = this.transformCurrencyValue(Currency.CODE_CURRENCY_USD, transfer.getCurrency().getCode(), 100D, exchangeRates);

		if (transfer.getAmount() > limitAmount) {
			return transfer.getAmount() * 0.005;
		} else {
			return transfer.getAmount() * 0.002;
		}
	}

	private void validateFundsOriginAccount(final Transfer transfer) {
		if (transfer.getOriginAccount().getBalance() - transfer.getAmount() < 0) {
			throw new IllegalArgumentException(ErrorType.insufficient_funds.name());
		}
	}

	private void validateMaxDailyTransfers(final Account originAccount) throws SystemException
	{
		final LocalDate now = LocalDate.now();
		final LocalTime min = LocalTime.MIN;
		final LocalTime max = LocalTime.MAX;
		final Date from = Date.from(LocalDateTime.of(now, min).atZone(ZoneId.systemDefault()).toInstant());
		final Date to = Date.from(LocalDateTime.of(now, max).atZone(ZoneId.systemDefault()).toInstant());

		final Long totalTransfersToday = this.transferRepository.totalAllByOriginAccountAndDateFromTo(originAccount, from, to);

		final Optional<Config> optConfig = this.configService.findByCode(Config.MAX_DAILY_TRANSFERS_CONFIG_CODE);
		if (optConfig.isEmpty()) {
			log.error("The parameter " + Config.MAX_DAILY_TRANSFERS_CONFIG_CODE + " is missing be configured.");
			throw new SystemException("The parameter " + Config.MAX_DAILY_TRANSFERS_CONFIG_CODE + " is missing be configured.");
		}

		final Long maxDailyTransfers = Long.parseLong(optConfig.get().getValue());
		if (totalTransfersToday >= maxDailyTransfers) {
			throw new IllegalArgumentException(ErrorType.limit_exceeded.name());
		}
	}
}
