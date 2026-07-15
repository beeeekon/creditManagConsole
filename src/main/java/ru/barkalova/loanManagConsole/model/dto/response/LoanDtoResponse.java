package ru.barkalova.loanManagConsole.model.dto.response;

import lombok.Data;
import ru.barkalova.loanManagConsole.model.enums.Currency;
import ru.barkalova.loanManagConsole.model.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanDtoResponse {

    private Long id;
    private Long clientId;
    private String loanNumber;
    private Currency currency;
    private BigDecimal amount;
    private Integer termMonths;
    private BigDecimal monthlyPayment;
    private BigDecimal interestRate;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
