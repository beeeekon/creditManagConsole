package ru.barkalova.loanManagConsole.model.dto.mapper;


import org.mapstruct.*;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoCreateRequest;
import ru.barkalova.loanManagConsole.model.dto.request.LoanDtoPatchRequest;
import ru.barkalova.loanManagConsole.model.dto.response.LoanDtoResponse;
import ru.barkalova.loanManagConsole.model.entity.Loan;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface LoanMapper {

    Loan toEntity(LoanDtoCreateRequest request);

    @Mapping(target = "clientId", source = "client.id")
    LoanDtoResponse toResponse(Loan loan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateLoanFromPatch(LoanDtoPatchRequest request, @MappingTarget Loan loan);

    List<LoanDtoResponse> toResponseList(List<Loan> loans);

}
