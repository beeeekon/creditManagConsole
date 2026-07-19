package ru.barkalova.loanManagConsole.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.LoanDtoResponse;
import ru.barkalova.loanManagConsole.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/clients/{clientId}/loans")
    public ResponseEntity<LoanDtoResponse> createLoan(
            @PathVariable Long clientId,
            @Valid @RequestBody LoanDtoCreateRequest request) {
        LoanDtoResponse response = loanService.createLoan(clientId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/clients/{clientId}/loans")
    public ResponseEntity<List<LoanDtoResponse>> getClientLoans(@PathVariable Long clientId) {
        List<LoanDtoResponse> response = loanService.getLoansByClientId(clientId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/loans/{loanId}")
    public ResponseEntity<LoanDtoResponse> patchLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody LoanDtoPatchRequest request) {
        LoanDtoResponse response = loanService.patchLoan(loanId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<LoanDtoResponse> getLoan(@PathVariable Long loanId) {
        LoanDtoResponse response = loanService.getLoanById(loanId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/loans/{loanId}/close")
    public ResponseEntity<LoanDtoResponse> closeLoan(@PathVariable Long loanId) {
        LoanDtoResponse response = loanService.closeLoan(loanId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/loans/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }
}
