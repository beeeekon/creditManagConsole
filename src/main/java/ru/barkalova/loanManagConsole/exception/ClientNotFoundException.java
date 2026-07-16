package ru.barkalova.loanManagConsole.exception;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException(String message){
        super(message);
    }

    public ClientNotFoundException(Long clientId){
        super("Клиент с id:" + clientId + " не найден");
    }

}
