package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.LoanDTO;
import com.example.ebankify.domain.entities.Loan;
import com.example.ebankify.domain.requests.LoanRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    LoanMapper Instance = Mappers.getMapper(LoanMapper.class);
    LoanDTO toDto(Loan loan);
    Loan toEntity(LoanDTO loanDto);
    Loan toEntity(LoanRequest loanRequest);

    List<LoanDTO> toDtoList(List<Loan> loans);
}
