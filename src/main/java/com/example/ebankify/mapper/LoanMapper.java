package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.LoanDTO;
import com.example.ebankify.domain.entities.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    LoanMapper Instance = Mappers.getMapper(LoanMapper.class);
    LoanDTO toDto(Loan loan);
    Loan toEntity(LoanDTO loanDto);
}
