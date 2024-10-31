package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",uses = { UserMapper.class })
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    AccountDTO toDto(Account account);
    List<AccountDTO> toDtoList(List<Account> accounts);
    Account toEntity(AccountRequest accountRequest);
    Account toEntity(AccountDTO accountDto);
}