package no.bankaxept.home.assignment.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import no.bankaxept.home.assignment.entity.BalanceEntity;
import no.bankaxept.home.assignment.entity.TransactionEntity;
import no.bankaxept.home.assignment.exception.BankValidationException;
import no.bankaxept.home.assignment.exception.InsufficientFundsException;
import no.bankaxept.home.assignment.model.Transaction;
import no.bankaxept.home.assignment.repository.BalanceRepository;
import no.bankaxept.home.assignment.repository.TransactionRepository;

/**
 * Service class to handle transaction related activity
 */

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService{

		private final BalanceRepository balanceRepository;
		private final TransactionRepository transactionRepository;
		private static Integer balance;
	    public RestTemplate restTemplate;
	    public ObjectMapper objectMapper;
	    

	    public PaymentServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper,
	    		BalanceRepository balanceRepository,TransactionRepository transactionRepository) {
	       
	        this.restTemplate = restTemplate;
	        this.objectMapper = objectMapper;
	        this.balanceRepository = balanceRepository;
	        this.transactionRepository = transactionRepository;
	    }
	    
	    @Override
	    public Map<String, Long> checkBigBank(Transaction transaction,String transactionUuid) {	
	
		StopWatch stopWatch = new StopWatch();
	    stopWatch.start();

	    long newBalance = checkBalance(transaction);
        
        if (transaction.amount < 200) {
			log.info("inside  TheBigBank{}");
			insert(transaction,transactionUuid,"small");	
            
        } else if (transaction.amount > 200) {
        	log.info("inside TheBigBank resttemplate");
            ResponseEntity<String> bigBankResponse = restTemplate.getForEntity("http://fake.bigbank.no/check/" + transaction.amount, String.class);
            String response = bigBankResponse.getBody();
            if (response.equalsIgnoreCase("Success")) {
            	insert(transaction,transactionUuid,"big");	
            } else {
                throw new BankValidationException(transaction.bank, "Not successful");
            }
        }
        

        stopWatch.stop();

        Map<String, Long> result = new HashMap<>();
        result.put("balance", newBalance);
        result.put("duration", stopWatch.getTotalTimeMillis());
        return result;
	}
	
	@Override
	public Map<String, Long> checkCashiers(Transaction transaction,String transactionUuid) throws RestClientException, URISyntaxException {
		
		StopWatch stopWatch = new StopWatch();
	    stopWatch.start();
	    
	    long newBalance = checkBalance(transaction);
	    
		if (transaction.amount < 100) {
			log.info("inside  TheCashiers{}");
			insert(transaction,transactionUuid,"small");
		}else if (transaction.amount > 100) {
			log.info("inside TheCashiers resttemplate");
			ResponseEntity<Object> bigBankResponse = restTemplate.getForEntity(new URI("http://fake.cashiers.no/payment/"
                    + transaction.cardNumber + "?amount=" + transaction.amount), Object.class);
            if (bigBankResponse.getStatusCodeValue() == 200) {
            	insert(transaction,transactionUuid,"big");
            } else {
                throw new BankValidationException(transaction.bank, "Error Status Code: " + bigBankResponse.getStatusCodeValue());
            }
		}
		
		stopWatch.stop();

        Map<String, Long> result = new HashMap<>();
        result.put("balance", newBalance);
        result.put("duration", stopWatch.getTotalTimeMillis());
        return result;
		
	}
	
	@Override
	public Map<String, Long> checkLoaners (Transaction transaction,String transactionUuid) throws RestClientException, URISyntaxException {
		
		StopWatch stopWatch = new StopWatch();
	    stopWatch.start();
	    
	    long newBalance = checkBalance(transaction);
	    
		if (transaction.amount < 100) {
			log.info("inside  Loaners{}");
			insert(transaction,transactionUuid,"small");
		}else if (transaction.amount > 100) {
			log.info("inside Loaners resttemplate");
			 LoanersRequest request = new LoanersRequest(transaction.cardNumber, transaction.amount);
                ResponseEntity<LoanersResponse> responseEntity = restTemplate.postForEntity(new URI("http://fake.loaners.no/payment/check"),
                        request, LoanersResponse.class);
                LoanersResponse loanersResponse = responseEntity.getBody();
                if (loanersResponse.responseCode == 1) {
                    if (loanersResponse.status == "Approved") {
                    	insert(transaction,transactionUuid,"big");
                    } else {
                        throw new BankValidationException(transaction.bank, "Transaction not approved. Status: " + loanersResponse.status);
                    }
                } else {
                    throw new BankValidationException(transaction.bank, "Error status code: " + loanersResponse.responseCode + ", with reason: " + loanersResponse.errorReason);
                }
		
		}
		stopWatch.stop();

        Map<String, Long> result = new HashMap<>();
        result.put("balance", newBalance);
        result.put("duration", stopWatch.getTotalTimeMillis());
        return result;
		
	}
	private Long checkBalance(Transaction transaction) {
		
		List<BalanceEntity> currentBalance = balanceRepository.checkBalance(transaction.cardNumber);

	    currentBalance.stream().forEach(q -> {
	    	balance = q.getAmount();        	
	    });
	    log.info("CurrentBalance {}",balance);
	    
	    if (balance < transaction.amount) {
	        throw new InsufficientFundsException(balance, transaction.amount, transaction.bank);
	    }
	    
	    long newBalance = balance - transaction.amount;
        balanceRepository.updateBalance(newBalance,transaction.cardNumber);
		
        return newBalance;
	}
	
	private void insert(Transaction transaction,String transactionUuid,String type) {
		
		TransactionEntity transactionEntity = TransactionEntity.builder()
				.bank(transaction.bank)
				.UUID(transactionUuid)
				.cardNumber(transaction.cardNumber)
				.amount(transaction.amount)
				.date(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())))
				.type(type)
				.build();
				
		transactionRepository.save(transactionEntity);
	}
	
	
	private static class LoanersResponse {
        public int responseCode;
        public String status;
        public String errorReason;

        public LoanersResponse(int responseCode, String status, String errorReason) {
            this.responseCode = responseCode;
            this.status = status;
            this.errorReason = errorReason;
        }
    }

    private static class LoanersRequest {
        public String cardNumber;
        public int amount;

        public LoanersRequest(String cardNumber, int amount) {
            this.cardNumber = cardNumber;
            this.amount = amount;
        }
    }

	
}
