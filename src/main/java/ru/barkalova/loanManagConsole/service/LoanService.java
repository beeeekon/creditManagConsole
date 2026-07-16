package ru.barkalova.loanManagConsole.service;

import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.LoanDtoResponse;

import java.util.List;

public interface LoanService {

    LoanDtoResponse createLoan(Long clientId, LoanDtoCreateRequest request);

    LoanDtoResponse patchLoan(Long loanId, LoanDtoPatchRequest request);

    LoanDtoResponse closeLoan(Long loanId);

    void deleteLoan(Long loanId);

    List<LoanDtoResponse> getLoansByClientId(Long clientId);

    LoanDtoResponse getLoanById(Long loanId);

}
