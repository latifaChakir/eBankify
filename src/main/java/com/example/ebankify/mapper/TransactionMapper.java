package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.TransactionDTO;
import com.example.ebankify.domain.entities.Transaction;
import com.example.ebankify.domain.requests.TransactionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper Instance= Mappers.getMapper(TransactionMapper.class);
    TransactionDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDto);
    Transaction toEntity(TransactionRequest transactionRequest);
    List<TransactionDTO> toDtoList(List<Transaction> transactions);
}
