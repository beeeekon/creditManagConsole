package ru.barkalova.loanManagConsole.model.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoCardResponse;
import ru.barkalova.loanManagConsole.model.dto.response.ClientDtoResponse;
import ru.barkalova.loanManagConsole.model.dto.response.LoanDtoResponse;
import ru.barkalova.loanManagConsole.model.entity.Client;
import ru.barkalova.loanManagConsole.model.entity.Loan;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ClientMapper.class, LoanMapper.class},  // инжектим другие мапперы
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClientWithLoanMapper {

    @Mapping(target = "client", source = "client")
    @Mapping(target = "loans", source = "loans")
    ClientDtoCardResponse toCardResponse(Client client, List<Loan> loans);
}