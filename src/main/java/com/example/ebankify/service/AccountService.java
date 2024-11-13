package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.entities.Bank;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.AccountRequest;
import com.example.ebankify.exception.BankNotFoundException;
import com.example.ebankify.mapper.AccountMapper;
import com.example.ebankify.mapper.BankMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.repository.BankRepository;
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
    private BankRepository bankRepository;
    private UserMapper userMapper;
    private BankMapper bankMapper;

    public AccountDTO createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
        System.out.println(accountRequest);
        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Bank bank= bankRepository.findById(accountRequest.getBankId())
                .orElseThrow(() -> new BankNotFoundException("Bank not found"));
        account.setUser(user);
        account.setBank(bank);
        Account savedAccount = accountRepository.save(account);
        AccountDTO accountDTO = accountMapper.toDto(savedAccount);
        accountDTO.setUser(userMapper.toDto(user));
        accountDTO.setBank(bankMapper.toDto(bank));
        return accountDTO;
    }
    public AccountDTO getAccountById(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new RuntimeException("Account not found");
        }
        AccountDTO accountDTO=accountMapper.toDto(account.get());
        accountDTO.setUser(userMapper.toDto(account.get().getUser()));
        accountDTO.setBank(bankMapper.toDto(account.get().getBank()));
        return accountDTO;
    }
    public AccountDTO updateAccount(Long accountId, AccountRequest updateRequest) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        User newUser = userRepository.findById(updateRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Bank newBank = bankRepository.findById(updateRequest.getBankId())
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        existingAccount.setAccountNumber(updateRequest.getAccountNumber());
        existingAccount.setBalance(updateRequest.getBalance());
        existingAccount.setStatus(updateRequest.getStatus());
        existingAccount.setUser(newUser);
        existingAccount.setBank(newBank);

        Account savedAccount = accountRepository.save(existingAccount);
        return accountMapper.toDto(savedAccount);
    }    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }
    public List<AccountDTO> findAll(){
        List<Account> accounts = accountRepository.findAllWithUser();
        return accountMapper.toDtoList(accounts);
    }
}
