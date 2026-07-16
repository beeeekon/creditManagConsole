package ru.barkalova.loanManagConsole.service;

import org.springframework.transaction.annotation.Transactional;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoSearchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoCardResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoSearchResponse;
import ru.barkalova.loanManagConsole.model.entity.Client;

public interface ClientService {


    ClientDtoResponse createClient(ClientDtoCreateRequest request);

    ClientDtoSearchResponse searchClients(ClientDtoSearchRequest request);

    ClientDtoCardResponse getClientCard(Long id);

    ClientDtoResponse patchClient(Long id, ClientDtoPatchRequest request);

    void deleteClient(Long id);

    Client getClientById(Long id);

    ClientDtoResponse getClientDtoResponseById(Long id);

}
