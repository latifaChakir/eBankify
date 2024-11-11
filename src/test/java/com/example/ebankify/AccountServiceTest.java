package com.example.ebankify;

import com.example.ebankify.domain.responses.AccountResponse;
import com.example.ebankify.mapper.AccountMapper;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
