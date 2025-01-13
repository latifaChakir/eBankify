package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.BankDto;
import com.example.ebankify.domain.entities.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankMapper {
    BankDto toDto(Bank bank);
    Bank toEntity(BankDto bankDto);

    List<BankDto> toDtoList(List<Bank> banks);
}
