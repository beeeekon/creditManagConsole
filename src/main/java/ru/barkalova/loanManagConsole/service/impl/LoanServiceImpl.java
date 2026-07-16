package ru.barkalova.loanManagConsole.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.barkalova.loanManagConsole.exception.ClientStatusException;
import ru.barkalova.loanManagConsole.exception.LoanNotFoundException;
import ru.barkalova.loanManagConsole.exception.LoanStatusException;
import ru.barkalova.loanManagConsole.model.dto.mapper.LoanMapper;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.LoanDtoResponse;
import ru.barkalova.loanManagConsole.model.entity.Client;
import ru.barkalova.loanManagConsole.model.entity.Loan;
import ru.barkalova.loanManagConsole.model.enums.ClientStatus;
import ru.barkalova.loanManagConsole.model.enums.LoanStatus;
import ru.barkalova.loanManagConsole.repository.LoanRepository;
import ru.barkalova.loanManagConsole.service.ClientService;
import ru.barkalova.loanManagConsole.service.LoanService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;


//СМ файл BUSINESS_RULES.txt
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final ClientService clientService;
    private final LoanMapper loanMapper;

    //базовые ставки
    private static final BigDecimal BASE_RATE = new BigDecimal("14.25");   // Ставка ЦБ на 2026 год
    private static final BigDecimal MARGIN = new BigDecimal("4.5");       // Маржа банка

    //скидки
    private static final BigDecimal YOUTH_DISCOUNT = new BigDecimal("1.0"); //молодежная скидка
    private static final BigDecimal FIRST_LOAN_DISCOUNT = new BigDecimal("0.3"); //за первый кредит в банке (кредитов нет никаких на момент создания кредита)
    private static final BigDecimal TERM_DISCOUNT_PER_SIX_MONTHS = new BigDecimal("0.02"); //за каждые 6 мес срока

    //параметры для скидок
    private static final int MIN_TERM_FOR_DISCOUNT = 24;// Минимальный срок для скидки (24 месяца)
    private static final int TERM_DISCOUNT_STEP = 6;    // Шаг для скидки (6 месяцев)
    private static final int MIN_AGE_FOR_YOUTH_DISCOUNT = 18;// Минимальный возраст для молодежной скидки
    private static final int MAX_AGE_FOR_YOUTH_DISCOUNT = 25;// Максимальный возраст для молодежной скидки


    @Override
    @Transactional
    public LoanDtoResponse createLoan(Long clientId, LoanDtoCreateRequest request) {
        Client client = clientService.getClientById(clientId);
        if(client.getStatus()!= ClientStatus.ACTIVE){
            throw new ClientStatusException(client.getStatus().toString());
        }

        Loan loan = loanMapper.toEntity(request);
        loan.setClient(client);

        BigDecimal interestRate = calculateInterestRate(client,request);
        loan.setInterestRate(interestRate);

        BigDecimal monthlyPayment = calculateMonthlyPayment(request.getAmount(),interestRate, request.getTermMonths());
        loan.setMonthlyPayment(monthlyPayment);

        loan.setLoanStatus(LoanStatus.ACTIVE);

        loan =loanRepository.save(loan);
        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public LoanDtoResponse patchLoan(Long loanId, LoanDtoPatchRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        Client client = loan.getClient();
        if (client.getStatus() == ClientStatus.CLOSED) {
            throw new ClientStatusException(client.getStatus().toString());
        }
        if (loan.getLoanStatus() == LoanStatus.CLOSED) {
            throw new LoanStatusException(loan.getLoanStatus().toString());
        }
        if (loan.getLoanStatus() == LoanStatus.CANCELLED) {
            throw new LoanStatusException(loan.getLoanStatus().toString());
        }
        loanMapper.updateLoanFromPatch(request, loan);
        if (request.getAmount() != null || request.getTermMonths() != null) {
            BigDecimal newPayment = calculateMonthlyPayment(loan.getAmount(), loan.getInterestRate(), loan.getTermMonths());
            loan.setMonthlyPayment(newPayment);
        }

        loan = loanRepository.save(loan);
        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public LoanDtoResponse closeLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        if (loan.getLoanStatus() == LoanStatus.CLOSED ||
                loan.getLoanStatus() == LoanStatus.CANCELLED)
        {
            throw new LoanStatusException(loan.getLoanStatus().toString());
        }


        Client client = loan.getClient();
        //Клиент закрыт, закрытие кредита невозможно
        if (client.getStatus() == ClientStatus.CLOSED) {
            throw new ClientStatusException(client.getStatus().toString());
        }

        loan.setLoanStatus(LoanStatus.CLOSED);
        loan = loanRepository.save(loan);
        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public void deleteLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.getLoanStatus() != LoanStatus.DRAFT) {
            throw new LoanStatusException("Удаление. Новый статус: ", loan.getLoanStatus().toString());
        }

        loan.setLoanStatus(LoanStatus.CANCELLED);
        loan = loanRepository.save(loan);
    }
    @Override
    @Transactional(readOnly = true)
    public List<LoanDtoResponse> getLoansByClientId(Long clientId) {

        //проверяем что существует
        clientService.getClientById(clientId);

        List<Loan> loans = loanRepository.findByClientId(clientId);
        return loanMapper.toResponseList(loans);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDtoResponse getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        return loanMapper.toResponse(loan);
    }

    //расчет процентной ставки
    private BigDecimal calculateInterestRate(Client client, LoanDtoCreateRequest request){

        BigDecimal rate = BASE_RATE.add(MARGIN);

        //молодежная скидка
        int age = calculateAge(client.getBirthDate());
        if(age>=MIN_AGE_FOR_YOUTH_DISCOUNT && age<= MAX_AGE_FOR_YOUTH_DISCOUNT)
            rate = rate.subtract(YOUTH_DISCOUNT);

        //скидка за первый кредит
        long loanCount = loanRepository.countByClientId(client.getId());
        if(loanCount == 0)
            rate = rate.subtract(FIRST_LOAN_DISCOUNT);

        //скидка за каждые 6 мес срока
        int countMonths = request.getTermMonths();
        if(countMonths >= MIN_TERM_FOR_DISCOUNT){
            int bonusCount = countMonths / TERM_DISCOUNT_STEP;
            BigDecimal bonus = TERM_DISCOUNT_PER_SIX_MONTHS.multiply(BigDecimal.valueOf(bonusCount));
            rate = rate.subtract(bonus);
        }

        return rate;
    }

    //расчет ежемесячного платежа
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal interestRate, Integer termMonths){
        BigDecimal interestAmount = amount
                .multiply(interestRate)
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        BigDecimal totalAmount = interestAmount.add(amount);
        return totalAmount.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
    }

    private int calculateAge(LocalDate birthDate){
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
