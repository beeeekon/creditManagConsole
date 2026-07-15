package ru.barkalova.loanManagConsole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.barkalova.loanManagConsole.model.entity.Loan;
import ru.barkalova.loanManagConsole.model.enums.LoanStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /*
    * GreaterThan - ключевое слово для сравнения >
    * */

    List<Loan> findByClientId(Long clientId);

    boolean existsByLoanNumber(String loanNumber);

    Optional<Loan> findByLoanNumber(String loanNumber);

    List<Loan> findByLoanStatus(LoanStatus status);

    List<Loan> findByClientIdAndLoanStatus(Long clientId, LoanStatus status);

    List<Loan> findByAmountGreaterThan(BigDecimal amount);

    long countByClientId(Long clientId);

    long countByClientIdAndLoanStatus(Long clientId, LoanStatus status);



}
