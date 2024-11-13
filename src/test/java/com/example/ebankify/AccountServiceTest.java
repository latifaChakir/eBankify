package com.example.ebankify;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.dtos.BankDto;
import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.entities.Bank;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.enums.AccountStatus;
import com.example.ebankify.domain.requests.AccountRequest;

import com.example.ebankify.mapper.AccountMapper;
import com.example.ebankify.mapper.BankMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.repository.BankRepository;
import com.example.ebankify.repository.UserRepository;
import com.example.ebankify.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BankMapper bankMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_ShouldReturnAccountDTO_WhenValidRequest() {
        // Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(1L);
        accountRequest.setBankId(1L);

        User user = new User();
        Bank bank = new Bank();
        Account account = new Account();
        AccountDTO accountDTO = new AccountDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bankRepository.findById(1L)).thenReturn(Optional.of(bank));
        when(accountMapper.toEntity(accountRequest)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        UserDto userDTO = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .build();

        BankDto bankDTO = BankDto.builder()
                .id(1L)
                .name("Bank XYZ")
                .build();

        when(userMapper.toDto(user)).thenReturn(userDTO);
        when(bankMapper.toDto(bank)).thenReturn(bankDTO);

        AccountDTO result = accountService.createAccount(accountRequest);
        assertNotNull(result);
        assertEquals(userDTO, result.getUser());
        assertEquals(bankDTO, result.getBank());
        verify(userRepository, times(1)).findById(1L);
        verify(bankRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAccountById_ShouldReturnAccountDTO_WhenAccountExists() {
        // Arrange
        Account account = new Account();
        account.setUser(new User());
        account.setBank(new Bank());
        AccountDTO accountDTO = new AccountDTO();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDTO);
        AccountDTO result = accountService.getAccountById(1L);
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteAccount_ShouldCallDeleteById_WhenAccountIdIsProvided() {
        Long accountId = 1L;
        accountService.deleteAccount(accountId);
        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    void testDeleteAccount_ShouldThrowException_WhenAccountDoesNotExist() {
        Long accountId = 1L;
        doThrow(new RuntimeException("Account not found")).when(accountRepository).deleteById(accountId);

        assertThrows(RuntimeException.class, () -> accountService.deleteAccount(accountId));
    }

    @Test
    void testFindAll_ShouldReturnList_OfAccountDTOs() {
        // Arrange
        Account account = new Account();
        account.setUser(new User());
        account.setBank(new Bank());
        AccountDTO accountDTO = new AccountDTO();

        when(accountRepository.findAllWithUser()).thenReturn(List.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        List<AccountDTO> result = accountService.findAll();
        assertNotNull(result);
        verify(accountRepository, times(1)).findAllWithUser();
    }

    @Test
    void testFindAll_ShouldReturnEmptyList_WhenNoAccountsExist() {
        when(accountRepository.findAllWithUser()).thenReturn(List.of());

        List<AccountDTO> result = accountService.findAll();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(accountRepository, times(1)).findAllWithUser();
    }

    @Test
    void testUpdateAccount_ShouldUpdateAccountDetails_WhenValidRequest() {
        Long accountId = 1L;
        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setAccountNumber("OLD12345");
        existingAccount.setBalance(100.0);
        existingAccount.setStatus(AccountStatus.BLOCKED);
        existingAccount.setUser(new User());
        existingAccount.setBank(new Bank());

        AccountRequest updateRequest = new AccountRequest();
        updateRequest.setUserId(2L);
        updateRequest.setBankId(2L);
        updateRequest.setBalance(500.0);
        updateRequest.setAccountNumber("ACC12345");
        updateRequest.setStatus(AccountStatus.ACTIVE);

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("Updated User");

        Bank newBank = new Bank();
        newBank.setId(2L);
        newBank.setName("Updated Bank");

        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("Updated User");

        BankDto bankDto = new BankDto();
        bankDto.setId(2L);
        bankDto.setName("Updated Bank");

        AccountDTO expectedDto = new AccountDTO();
        expectedDto.setId(accountId);
        expectedDto.setAccountNumber("ACC12345");
        expectedDto.setBalance(500.0);
        expectedDto.setStatus(AccountStatus.ACTIVE);
        expectedDto.setUser(userDto);
        expectedDto.setBank(bankDto);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(bankRepository.findById(2L)).thenReturn(Optional.of(newBank));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);
        when(accountMapper.toDto(any(Account.class))).thenReturn(expectedDto);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);
        when(bankMapper.toDto(any(Bank.class))).thenReturn(bankDto);

        AccountDTO result = accountService.updateAccount(accountId, updateRequest);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getAccountNumber(), result.getAccountNumber());
        assertEquals(expectedDto.getBalance(), result.getBalance());
        assertEquals(expectedDto.getStatus(), result.getStatus());
        assertEquals(expectedDto.getUser().getId(), result.getUser().getId());
        assertEquals(expectedDto.getBank().getId(), result.getBank().getId());

        verify(accountRepository).findById(accountId);
        verify(bankRepository).findById(2L);
        verify(userRepository).findById(2L);
        verify(accountRepository).save(any(Account.class));
    }
}