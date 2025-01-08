package com.example.ebankify.service;

import com.example.ebankify.domain.vm.StatisticResponseVM;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.repository.BankRepository;
import com.example.ebankify.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public StatisticResponseVM getStatistics() {
        long users = userRepository.countByRoleId(2);
        long employees = userRepository.countByRoleId(3);
        long administrators = userRepository.countByRoleId(1);
        long accounts = accountRepository.count();
        long banks = bankRepository.count();
        return new StatisticResponseVM(users, employees, administrators, accounts, banks);
    }

}
