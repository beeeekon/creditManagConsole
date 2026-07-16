package ru.barkalova.loanManagConsole.exception;

public class ClientStatusException extends RuntimeException {
    public ClientStatusException(String clientStatus) {

        super("Операция отклонена, статус клиента: " + clientStatus);
    }

    public ClientStatusException(String message, String clientStatus){
        super(message + clientStatus);
    }
}
