package ru.barkalova.loanManagConsole.model.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import ru.barkalova.loanManagConsole.model.enums.ClientStatus;


//что приходит от клиента на замену данных
//какие поля null такие не меняем
@Data
public class ClientDtoPatchRequest {

    private String firstName;//причем в сервисе проверка на ""

    private String lastName;

    private String middleName;//если придет "" так и внести

    private String passportNumber;

    @Email(message = "Некорректный email")
    private String email;

    private ClientStatus status;
}
