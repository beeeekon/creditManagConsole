package ru.barkalova.loanManagConsole.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import ru.barkalova.loanManagConsole.model.enums.ClientStatus;

import java.time.LocalDate;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 50)
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Past(message = "Дата рождения должна быть в прошлом")
    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "passport_number", nullable = false, unique = true)
    private String passportNumber;

    @NotNull
    @Email(message = "Некорректный формат email")
    @Column( nullable = false, unique = true)
    private String email;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status;
}
