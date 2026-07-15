package ru.barkalova.loanManagConsole.model.dto.response;

import lombok.Data;
import ru.barkalova.loanManagConsole.model.enums.ClientStatus;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class ClientDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String passportNumber;
    private String email;
    private ClientStatus status;

}
