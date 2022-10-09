package no.bankaxept.home.assignment.service;

import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.web.client.RestClientException;

import no.bankaxept.home.assignment.model.Transaction;

public interface PaymentService {

	public Map<String, Long> checkBigBank(Transaction transaction,String transactionUuid);
	public Map<String, Long> checkCashiers(Transaction transaction,String transactionUuid) throws RestClientException, URISyntaxException;
	public Map<String, Long> checkLoaners(Transaction transaction,String transactionUuid)throws RestClientException, URISyntaxException;
}
