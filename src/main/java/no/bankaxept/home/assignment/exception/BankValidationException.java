package no.bankaxept.home.assignment.exception;

public class BankValidationException extends RuntimeException {
    public String bankName;
    public String message;

    public BankValidationException(String bankName, String message) {
        this.bankName = bankName;
        this.message = message;
    }
}
