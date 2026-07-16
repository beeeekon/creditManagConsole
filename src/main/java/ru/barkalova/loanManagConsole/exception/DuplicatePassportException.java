package ru.barkalova.loanManagConsole.exception;

public class DuplicatePassportException extends RuntimeException {
    public DuplicatePassportException(String passportNumber) {

        super("Номер паспорта:" + passportNumber + " уже используется");
    }
}
