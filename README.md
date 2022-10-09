# Payment-Service

A simplified banking transaction system
Criteria for transaction accepting/rejecting:
- if requested amount is bigger than current balance, the transaction should be rejected
- for `small` transaction, where amount is below threshold (defined for each bank individually), no additional check is necessary
- for `big` transaction, where amount is above threshold (defined for each bank individually), additional REST call towards bank system is necessary

# Prerequisites
- Java 8
- Maven 
- IDE (IntelliJ, Eclipse)

# Technical design
System consists of two database tables:
- `balance` which keeps track of current balance for every card
- `transaction` which keeps track of all successful transactions in the system

in-memory H2 database is used

# The endpoints available
 POST http://localhost:8080/payment/pay - Make transaction

# Request
Example :-
For Loaners :-

	{"cardNumber":"004","bank":"TheLoaners","amount":99}
For BigBank :-

	{"cardNumber":"004","bank":"TheBigBank","amount":199}
For Cashiers :-

	{"cardNumber":"004","bank":"TheCashiers","amount":99}
	
# Response
Example :-
For Loaners :-

	{"cardNumber":"004","bank":"TheLoaners","amount":"99","transactionTimestamp":"Sun Oct 09 21:44:24 CEST 	2022","currentAccountBalance":"3","duration":"10"}
For BigBank :-
	
	{"cardNumber":"004","bank":"TheBigBank","amount":"199","transactionTimestamp":"Sun Oct 09 21:43:53 CEST 2022","currentAccountBalance":"201","duration":"285"}
For Cashiers :-
	
	{"cardNumber":"004","bank":"TheCashiers","amount":"99","transactionTimestamp":"Sun Oct 09 21:44:15 CEST 2022","currentAccountBalance":"102","duration":"8"}

# Implementation Details
	1. payment-model and payment-web combined to a single payment-service Springboot Microservice application
	2. Introduced an MVC structure
	3. The project uses spring boot with JPA.
	4. To improve the code and for better redability changed the code to functions in the implementation class
	5. Added entity,constants class
	6. Added test class
	 

# Method Details

	checkBigBank
	-------------
	1. Method accepts transaction and transactionUuid.
	2. Invoke checkBalance() for card number for the transaction
	3. If the request is for the bank "TheBigBank" and the transaction amount is less than 200 then transaction 		will accept and save to transaction entity
	4. If the transaction amount is greater than 200 then it will invoke the REST API 		http://fake.bigbank.no/check/	${amount}
	5.	If the response is not valid will throw an exception
	
	checkCashiers
	-------------
	1. Method accepts transaction and transactionUuid.
	2. Invoke checkBalance() for card number for the transaction
	3. If the request is for the bank "TheCashiers" and the transaction amount is less than 100 then transaction 		will accept and save to transaction entity
	4. If the transaction amount is greater than 100 then it will invoke the REST API
		http://fake.cashiers.no/payment/${cardNumber}?amount=${amount}
	5. If the response is not valid will throw an exception
		
	checkLoaners
	-------------
	1. Method accepts transaction and transactionUuid.
	2. Invoke checkBalance() for card number for the transaction
	3. If the request is for the bank "Loaners" and the transaction amount is less than 100 then transaction 		will accept and save to transaction entity
	4. If the transaction amount is greater than 100 then it will invoke the REST API
		http://fake.loaners.no/payment/check
	5.	If the response is not valid will throw an exception
		
	checkBalance
	------------
	1. Method accepts transaction.
	2. Check the current balance for card number for the transaction
	3. If the balance is insufficient will throw an exception
	4. If transaction is accepted then will update the new balance to balance entity
		
# Issues and Bugs

	1. When i tried to load the H2 console i got the below details
		There was an unexpected error (type=Not Found, status=404).
		Solution :-  In application.properties file added this property spring.h2.console.enabled=true 
		
	2. While inserting into transaction table	cardnumber and uuid values are not inserting properly
		There is a value mismatch between two columns
		Changed the code accordingly
		
	3. RestTemplate REST APIs are not calling correctly
		UnknownHostException for all three greater threshold values
		
	4. There is a mistake in the JSON string format in the response. A "," is missing
		Changed the code accordingly
		
	5. Have mistake in threshold value condition for "TheBigBanks" according to the criteria mentioned
		Changed the code accordingly
		
	6. In the case of the RestTemplate , there is a problem of getting the actual response. So there is a 		confusion in the code to how refactor should do.
		





