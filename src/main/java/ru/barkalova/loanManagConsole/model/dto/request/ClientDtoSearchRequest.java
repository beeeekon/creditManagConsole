package ru.barkalova.loanManagConsole.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

//что приходит с фронта для поиска клиента
@Data
public class ClientDtoSearchRequest {

    private String firstName;

    private String lastName;

    private String passportNumber;


}
