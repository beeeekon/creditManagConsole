package ru.barkalova.loanManagConsole.model.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.ClientDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoResponse;
import ru.barkalova.loanManagConsole.model.entity.Client;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDtoCreateRequest request);

    ClientDtoResponse toResponse(Client client);

    // @MappingTarget - говорит: обнови существующий объект
    // nullValuePropertyMappingStrategy = IGNORE - не трогать поля с null
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromPatch(ClientDtoPatchRequest request, @MappingTarget Client client);

    List<ClientDtoResponse> toResponseList(List<Client> clients);
}
