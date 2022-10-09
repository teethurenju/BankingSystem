package no.bankaxept.home.assignment.web;

import no.bankaxept.home.assignment.constants.Constants;
import no.bankaxept.home.assignment.exception.BankValidationException;
import no.bankaxept.home.assignment.model.Transaction;
import no.bankaxept.home.assignment.service.PaymentService;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping(path = "/pay")
    @ResponseBody
    public String pay(@RequestBody Transaction transaction) throws IOException {
        Map<String, Long> payment = null;
        try {
            
            if(transaction.bank.equalsIgnoreCase(Constants.TheBigBank)) {
            	payment = paymentService.checkBigBank(transaction, UUID.randomUUID().toString());
            }else if(transaction.bank.equalsIgnoreCase(Constants.TheCashiers)) {
            	payment = paymentService.checkCashiers(transaction, UUID.randomUUID().toString());
            }else if(transaction.bank.equalsIgnoreCase(Constants.TheLoaners)) {
            	payment = paymentService.checkLoaners(transaction, UUID.randomUUID().toString());
            }
        } catch (DataAccessException e) {
            return "{" +
                    "\"bank\":\"" + transaction.bank + "\"," +
                    "\"error\":\"Database error.\"" +
                    "\"reason\":\"" + e.toString() + "\"" +
                    "}";
        } catch (URISyntaxException e) {
            return "{" +
                    "\"bank\":\"" + transaction.bank + "\"," +
                    "\"error\":\"URI error.\"" +
                    "\"reason\":\"" + e.toString() + "\"" +
                    "}";
        } catch (BankValidationException e) {
            e.printStackTrace();
            return "{" +
                    "\"error\":\"Bank validation failed.\"," +
                    "\"bank\":\"" + e.bankName + "\"," +
                    "\"reason\":\"" + e.message + "\"," +
                    "}";
        }

        return "{" +
                "\"cardNumber\":\"" + transaction.cardNumber + "\"," +
                "\"bank\":\"" + transaction.bank + "\"," +
                "\"amount\":\"" + transaction.amount + "\"," +
                "\"transactionTimestamp\":\"" + new Date() + "\"," +
                "\"currentAccountBalance\":\"" + payment.get("balance") + "\"," +
                "\"duration\":\"" + payment.get("duration") + "\"" +
                "}";
    }

}
