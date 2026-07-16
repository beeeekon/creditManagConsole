package ru.barkalova.loanManagConsole.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(String message) {
        super(message);
    }

    public LoanNotFoundException(Long loanId) {
        super("Кредит с id:" + loanId + " не найден");
    }

}
