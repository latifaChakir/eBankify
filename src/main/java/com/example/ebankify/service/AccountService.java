package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.AccountRequest;
import com.example.ebankify.mapper.AccountMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private  AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public AccountDTO createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);
        Account savedAccount = accountRepository.save(account);
        AccountDTO accountDTO = accountMapper.toDto(savedAccount);
        accountDTO.setUser(userMapper.toDto(user));
        return accountDTO;
    }
    public AccountDTO getAccountById(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        AccountDTO accountDTO=accountMapper.toDto(account.get());
        accountDTO.setUser(userMapper.toDto(account.get().getUser()));
        return accountDTO;
    }
    public AccountDTO updateAccount(Long accountId, AccountRequest accountRequest) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOptional.get();
        account.setBalance(accountRequest.getBalance());
        account.setAccountNumber(accountRequest.getAccountNumber());
        account.setStatus(accountRequest.getStatus());

        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);

        AccountDTO accountDTO = accountMapper.toDto(savedAccount);
        accountDTO.setUser(userMapper.toDto(user)); // Conversion explicite de User vers UserDto

        return accountDTO;
    }
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }
    public List<AccountDTO> findAll(){
        List<Account> accounts = accountRepository.findAllWithUser();
        return accountMapper.toDtoList(accounts);
    }
}
