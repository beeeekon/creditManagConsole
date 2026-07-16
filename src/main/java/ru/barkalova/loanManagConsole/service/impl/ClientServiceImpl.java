package ru.barkalova.loanManagConsole.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.barkalova.loanManagConsole.exception.*;
import ru.barkalova.loanManagConsole.model.dto.mapper.ClientMapper;
import ru.barkalova.loanManagConsole.model.dto.mapper.ClientWithLoanMapper;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoSearchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoCardResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoSearchResponse;
import ru.barkalova.loanManagConsole.model.entity.Client;
import ru.barkalova.loanManagConsole.model.entity.Loan;
import ru.barkalova.loanManagConsole.model.enums.ClientStatus;
import ru.barkalova.loanManagConsole.model.enums.LoanStatus;
import ru.barkalova.loanManagConsole.repository.ClientRepository;
import ru.barkalova.loanManagConsole.repository.LoanRepository;
import ru.barkalova.loanManagConsole.service.ClientService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;
    private final ClientMapper clientMapper;
    private final ClientWithLoanMapper clientWithLoanMapper;

    @Override
    @Transactional
    public ClientDtoResponse createClient(ClientDtoCreateRequest request) {
        if(clientRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException(request.getEmail());
        }
        if(clientRepository.existsByPassportNumber(request.getPassportNumber())){
            throw new DuplicatePassportException(request.getPassportNumber());
        }

        Client client = clientMapper.toEntity(request);
        client.setStatus(ClientStatus.ACTIVE);
        client = clientRepository.save(client);
        return clientMapper.toResponse(client);
    }

    //trim убирает пробелы в начале и конце строки
    @Override
    @Transactional(readOnly = true)
    public ClientDtoSearchResponse searchClients(ClientDtoSearchRequest request) {
        List<Client> clients = new ArrayList<>();
        //поиск по паспорту
        if(hasText(request.getPassportNumber())){
            clients = clientRepository.findByPassportNumberContainingIgnoreCase(request.getPassportNumber().trim());
        }
        //поиск по имени и фамилии
        else if (hasText(request.getFirstName())&&hasText(request.getLastName())) {
            clients = clientRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                    request.getFirstName().trim(),
                    request.getLastName().trim()
            );
        }
        //поиск по имени
        else if (hasText(request.getFirstName())) {
            clients = clientRepository.findByFirstNameContainingIgnoreCase(request.getFirstName());
        }
        //поиск по фамилии
        else if (hasText(request.getLastName())) {
            clients = clientRepository.findByLastNameContainingIgnoreCase(request.getLastName());
        }

        //ничего не передали
        else
            clients = clientRepository.findAll();

        ClientDtoSearchResponse response = new ClientDtoSearchResponse();
        response.setClients(clientMapper.toResponseList(clients));
        response.setTotalCount(clients.size());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDtoCardResponse getClientCard(Long id) {
        Client client = clientRepository.findById(id).
                orElseThrow(()->new ClientNotFoundException(id));
        List<Loan> loans = loanRepository.findByClientId(id);

        return clientWithLoanMapper.toCardResponse(client, loans);
    }


    //ifPresent - если найден
    //existing - найденный клиент
    @Override
    @Transactional
    public ClientDtoResponse patchClient(Long id, ClientDtoPatchRequest request) {

        Client client = clientRepository.findById(id).
                orElseThrow(()->new ClientNotFoundException(id));
        if(client.getStatus() == ClientStatus.CLOSED){
            throw new ClientStatusException(client.getStatus().toString());
        }
        if(hasText(request.getEmail())){
            clientRepository.findByEmail(request.getEmail()).
                    ifPresent( existing ->{
                        if(!existing.getId().equals(id)){
                            throw new DuplicateEmailException(request.getEmail());
                        }
                    }
                    );
        }
        if (request.getPassportNumber() != null && !request.getPassportNumber().isBlank()) {
            clientRepository.findByPassportNumber(request.getPassportNumber())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new DuplicatePassportException(request.getPassportNumber());
                        }
                    });
        }

        clientMapper.updateClientFromPatch(request, client);
        client = clientRepository.save(client);

        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).
                orElseThrow(()->new ClientNotFoundException(id));
        long activeLoans = loanRepository.countByClientIdAndLoanStatus(id, LoanStatus.ACTIVE);
        if(activeLoans > 0){
            throw new ValidationException(
                    "Нельзя удалить клиента с активными кредитами. Активных кредитов: " + activeLoans
            );
        }
        client.setStatus(ClientStatus.CLOSED);
        clientRepository.save(client);

    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientById(Long id) {

        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDtoResponse getClientDtoResponseById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        return clientMapper.toResponse(client);
    }

    //строка не null и не пустая ""
    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }
}
