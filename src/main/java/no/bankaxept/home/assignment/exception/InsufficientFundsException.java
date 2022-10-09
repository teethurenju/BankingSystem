package no.bankaxept.home.assignment.exception;

public class InsufficientFundsException extends RuntimeException {
    public int currentBalance;
    public int amount;
    public String bankName;

    public InsufficientFundsException(int currentBalance, int amount, String bankName) {
        this.currentBalance = currentBalance;
        this.amount = amount;
        this.bankName = bankName;
    }
}
