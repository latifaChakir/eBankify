package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.TransactionDTO;
import com.example.ebankify.domain.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper Instance= Mappers.getMapper(TransactionMapper.class);
    TransactionDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDto);
}
