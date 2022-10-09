package no.bankaxept.home.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseBody
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException exception) {
        exception.printStackTrace();
        String response = "{" +
                "\"error\":\"Insufficient funds.\"," +
                "\"bank\":\"" + exception.bankName + "\"," +
                "\"reason\":\" You tried to pay '" + exception.amount + "\"', but have only '" + exception.currentBalance + "' available.\"" +
                "}";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ExceptionHandler(BankValidationException.class)
    @ResponseBody
    public ResponseEntity<String> handleBankValidationException(BankValidationException exception) {
        exception.printStackTrace();
        String response = "{" +
                "\"error\":\"Validate Bank.\"," +
                "\"bank\":\"" + exception.bankName + "\"," +
                "\"reason\":\" Unknown Host '" + exception.message + "\"'}";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<String> handleBusinessException(BusinessException exception) {
        exception.printStackTrace();
        String response = "{" +
                "\"error\":\"Exception\"," +
                "\"reason\":\" BusinessException '" + exception.getMessage() + "\"'}";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
