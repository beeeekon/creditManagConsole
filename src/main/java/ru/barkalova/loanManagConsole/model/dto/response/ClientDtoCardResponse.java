package ru.barkalova.loanManagConsole.model.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ClientDtoCardResponse {

    private ClientDtoResponse client;
    private List<LoanDtoResponse> loans;

}
