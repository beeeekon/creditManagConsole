package ru.barkalova.loanManagConsole.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoSearchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoCardResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoSearchResponse;
import ru.barkalova.loanManagConsole.service.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;



    //@Valid - проверить все ограничения, которые прописаны в dto, в request
    //@ModelAttribute - для GET собрать из запроса URL пользователя данные и распихать по нужным полям
    //@RequestBody - для остальных, собрать данные из JSON
    //@PathVariable - извлекает id из url
    @GetMapping("/search")
    public ResponseEntity<ClientDtoSearchResponse> searchClients(
            @Valid @ModelAttribute ClientDtoSearchRequest request
    ){
        ClientDtoSearchResponse response = clientService.searchClients(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}/card")
    public ResponseEntity<ClientDtoCardResponse> getClientCard(@PathVariable Long id) {
        ClientDtoCardResponse response = clientService.getClientCard(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ClientDtoResponse> createClient(
            @Valid @RequestBody ClientDtoCreateRequest request) {
        ClientDtoResponse response = clientService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientDtoResponse> patchClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientDtoPatchRequest request) {
        ClientDtoResponse response = clientService.patchClient(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
