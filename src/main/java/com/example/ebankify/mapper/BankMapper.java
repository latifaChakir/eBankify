package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.BankDto;
import com.example.ebankify.domain.entities.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BankMapper {
    BankMapper Instance = Mappers.getMapper(BankMapper.class);
    BankDto toDto(Bank bank);
    Bank toEntity(BankDto bankDto);

}
