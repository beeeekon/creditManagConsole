package ru.barkalova.loanManagConsole.exception;

public class LoanStatusException extends RuntimeException {
    public LoanStatusException(String loanStatus) {

        super("Операция отклонена, статус кредита: " + loanStatus);
    }
}
