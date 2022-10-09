package no.bankaxept.home.assignment.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.bankaxept.home.assignment.entity.BalanceEntity;
import no.bankaxept.home.assignment.model.Transaction;
import no.bankaxept.home.assignment.repository.TransactionRepository;
import no.bankaxept.home.assignment.service.BalanceRepoImpl;
import no.bankaxept.home.assignment.service.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {


	@Mock
	private BalanceRepoImpl balanceRepoImpl; 

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private RestTemplate restTemplate;
	
	@Autowired
    public ObjectMapper objectMapper;
	
	
	private  PaymentServiceImpl paymentServiceImpl = null;
	
		  
			
			@Test
			public void testCheckBigBankBelowThreshold() throws Exception {
				
				paymentServiceImpl = new PaymentServiceImpl(restTemplate, objectMapper, balanceRepoImpl, transactionRepository);
				
				Transaction transaction = new Transaction();
				transaction.cardNumber ="005";
				transaction.bank="TheBigBank";
				transaction.amount=199;

			   JSONObject json = new JSONObject();
			   json.put("test", "value");
			   json.put("content", json);

			   BalanceEntity balanceEntity = BalanceEntity.builder().ID(5).amount(500).cardNumber("005").build();
			   List<BalanceEntity> balanceList = new ArrayList<>();
			   balanceList.add(balanceEntity);		
			  
			   
			   
		        
		        Mockito.when(balanceRepoImpl.checkBalance(Mockito.anyString())).thenReturn(balanceList);
				Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(null);
						
				Map<String, Long> actual = paymentServiceImpl.checkBigBank(transaction, UUID.randomUUID().toString());
				   
				Map<String, Long> result = new HashMap<>();
		        result.put("balance", 301L);
		        result.put("duration", actual.get("duration"));
				  
				   System.out.println("actual"+actual);
				   Assertions.assertEquals(result, actual);
			}
			
			
			@Test
			public void testCheckBigBankGreaterThreshold() throws Exception {
				
				paymentServiceImpl = new PaymentServiceImpl(restTemplate, objectMapper, balanceRepoImpl, transactionRepository);
				
				Transaction transaction = new Transaction();
				transaction.cardNumber ="005";
				transaction.bank="TheBigBank";
				transaction.amount=202;

			   JSONObject json = new JSONObject();
			   json.put("test", "value");
			   json.put("content", json);

			   BalanceEntity balanceEntity = BalanceEntity.builder().ID(5).amount(500).cardNumber("005").build();
			   List<BalanceEntity> balanceList = new ArrayList<>();
			   balanceList.add(balanceEntity);	
			   
		        Mockito.when(balanceRepoImpl.checkBalance(Mockito.anyString())).thenReturn(balanceList);
				Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(null);
				
				Mockito.when(restTemplate.getForEntity("http://fake.bigbank.no/check/202", String.class))
				   .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));	
				
				Map<String, Long> actual = paymentServiceImpl.checkBigBank(transaction, UUID.randomUUID().toString());
				
				Map<String, Long> result = new HashMap<>();
		        result.put("balance", 298L);
		        result.put("duration", actual.get("duration"));
				  
				   System.out.println("actual"+actual);
				   Assertions.assertEquals(result, actual);
			}
			
			@Test
			public void testCheckCashiersBelowThreshold() throws Exception {
				
				paymentServiceImpl = new PaymentServiceImpl(restTemplate, objectMapper, balanceRepoImpl, transactionRepository);
				
				Transaction transaction = new Transaction();
				transaction.cardNumber ="005";
				transaction.bank="TheBigBank";
				transaction.amount=99;

			   JSONObject json = new JSONObject();
			   json.put("test", "value");
			   json.put("content", json);

			   BalanceEntity balanceEntity = BalanceEntity.builder().ID(5).amount(500).cardNumber("005").build();
			   List<BalanceEntity> balanceList = new ArrayList<>();
			   balanceList.add(balanceEntity);		
			  
		        
		        Mockito.when(balanceRepoImpl.checkBalance(Mockito.anyString())).thenReturn(balanceList);
				Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(null);
						
				Map<String, Long> actual = paymentServiceImpl.checkCashiers(transaction, UUID.randomUUID().toString());
				   
				Map<String, Long> result = new HashMap<>();
		        result.put("balance", 401L);
		        result.put("duration", actual.get("duration"));
				  
				   System.out.println("actual"+actual);
				   Assertions.assertEquals(result, actual);
			}
			
			@Test
			public void testCheckCashiersGreaterThreshold() throws Exception {
				
				paymentServiceImpl = new PaymentServiceImpl(restTemplate, objectMapper, balanceRepoImpl, transactionRepository);
				
				Transaction transaction = new Transaction();
				transaction.cardNumber ="005";
				transaction.bank="TheBigBank";
				transaction.amount=101;

			   JSONObject json = new JSONObject();
			   json.put("test", "value");
			   json.put("content", json);

			   BalanceEntity balanceEntity = BalanceEntity.builder().ID(5).amount(500).cardNumber("005").build();
			   List<BalanceEntity> balanceList = new ArrayList<>();
			   balanceList.add(balanceEntity);	
			   
		        Mockito.when(balanceRepoImpl.checkBalance(Mockito.anyString())).thenReturn(balanceList);
				Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(null);
				
				Mockito.when(restTemplate.getForEntity(new URI("http://fake.cashiers.no/payment/005?amount=101"), Object.class))
				   .thenReturn(new ResponseEntity<>( HttpStatus.OK));	
				
				Map<String, Long> actual = paymentServiceImpl.checkCashiers(transaction, UUID.randomUUID().toString());
				
				Map<String, Long> result = new HashMap<>();
		        result.put("balance", 399L);
		        result.put("duration", actual.get("duration"));
				  
				   System.out.println("actual"+actual);
				   Assertions.assertEquals(result, actual);
			}
			
			@Test
			public void testCheckLoanersBelowThreshold() throws Exception {
				
				paymentServiceImpl = new PaymentServiceImpl(restTemplate, objectMapper, balanceRepoImpl, transactionRepository);
				
				Transaction transaction = new Transaction();
				transaction.cardNumber ="005";
				transaction.bank="TheBigBank";
				transaction.amount=99;

			   JSONObject json = new JSONObject();
			   json.put("test", "value");
			   json.put("content", json);

			   BalanceEntity balanceEntity = BalanceEntity.builder().ID(5).amount(500).cardNumber("005").build();
			   List<BalanceEntity> balanceList = new ArrayList<>();
			   balanceList.add(balanceEntity);		
			  
		        
		        Mockito.when(balanceRepoImpl.checkBalance(Mockito.anyString())).thenReturn(balanceList);
				Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(null);
						
				Map<String, Long> actual = paymentServiceImpl.checkLoaners(transaction, UUID.randomUUID().toString());
				   
				Map<String, Long> result = new HashMap<>();
		        result.put("balance", 401L);
		        result.put("duration", actual.get("duration"));
				  
				   System.out.println("actual"+actual);
				   Assertions.assertEquals(result, actual);
			}
			
			

}
