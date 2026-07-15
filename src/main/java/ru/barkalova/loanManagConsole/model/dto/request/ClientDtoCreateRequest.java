package ru.barkalova.loanManagConsole.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;


import java.time.LocalDate;

// что приходит с фронта для создания записи о клиенте
@Data
public class ClientDtoCreateRequest {

    @NotBlank(message = "Обязательное поле")
    private String firstName;

    @NotBlank(message = "Обязательное поле")
    private String lastName;

    private String middleName;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotBlank(message = "Обязательное поле")
    private String passportNumber;

    @NotBlank(message = "Обязательное поле")
    @Email(message = "Некорректный формат email")
    private String email;
}
