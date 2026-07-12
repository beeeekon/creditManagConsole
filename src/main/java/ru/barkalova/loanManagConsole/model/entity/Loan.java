package ru.barkalova.loanManagConsole.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ru.barkalova.loanManagConsole.model.enums.Currency;
import ru.barkalova.loanManagConsole.model.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;                                     //id кредита

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;                               //id клиента внешний ключ

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "loan_number", nullable = false,updatable = false, unique = true)
    private String loanNumber;                          //номер кредита - бизнес ключ,
                                                        // который генерируется по бизнес-логике банка

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;                          //валюта


    @NotNull
    @DecimalMin(value = "0.00", message = "Сумма не может быть отрицательной")
    @Digits(integer = 15, fraction = 2) // Защита формата денег (макс 15 знаков до запятой, 2 после)
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal amount;                          //сумма
                                                        //BigDecimal - спец тип для денег в java,
                                                        // иначе будут теряться копейки

    @NotNull
    @Min(value = 0, message = "Срок не может быть отрицательным")
    @Column(name = "term_months", nullable = false)
    private Integer termMonths;                         //количество в месяцах

    @NotNull
    @DecimalMin(value = "0.00", message = "Платеж не может быть отрицательным")
    @Digits(integer = 15, fraction = 2)
    @Column(name = "monthly_payment", nullable = false, precision = 17, scale = 2)
    private BigDecimal monthlyPayment;                  //ежемесячный платеж


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status", nullable = false)
    private LoanStatus loanStatus;                      //статусы кредита


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        generateLoanNumber();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    private void generateLoanNumber(){
        //формат LN-ГГГГММДД-UUID
        if (this.loanNumber == null) {
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
            this.loanNumber = "LN-" + datePart +"-"+ uuid;
        }
    }

}
