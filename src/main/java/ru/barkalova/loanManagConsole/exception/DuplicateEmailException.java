package ru.barkalova.loanManagConsole.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {

        super("Email:" + email + " уже используется");
    }
}
