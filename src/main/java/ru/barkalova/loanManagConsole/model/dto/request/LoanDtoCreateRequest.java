package ru.barkalova.loanManagConsole.model.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.barkalova.loanManagConsole.model.enums.Currency;

import java.math.BigDecimal;

//что приходит с фронта для создания кредита
@Data
public class LoanDtoCreateRequest {

    @NotNull(message = "Обязательное поле")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull(message = "Обязательное поле")
    @DecimalMin(value = "0.01", message = "Сумма не может быть отрицательной")
    private BigDecimal amount;

    @NotNull(message = "Обязательное поле")
    @Min(value = 1, message = "Срок не может быть отрицательным")
    private Integer termMonths;

}
