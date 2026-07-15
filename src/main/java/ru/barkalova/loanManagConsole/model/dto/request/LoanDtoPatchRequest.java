package ru.barkalova.loanManagConsole.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import ru.barkalova.loanManagConsole.model.enums.LoanStatus;

import java.math.BigDecimal;

//что приходит с фронта для частичного изменения кредита
//все поля nullable: null = "не менять это поле"
@Data
public class LoanDtoPatchRequest {

    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    private BigDecimal amount;

    @DecimalMin(value = "0", message = "Количество месяцев не может быть отрицательным")
    private Integer termMonths;

    private LoanStatus status;
}
