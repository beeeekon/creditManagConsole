package ru.barkalova.loanManagConsole.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ClientDtoSearchResponse {

    private List<ClientDtoResponse> clients;

    private int totalCount;
}
