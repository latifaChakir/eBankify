package com.example.ebankify;

import com.example.ebankify.domain.dtos.AccountDTO;
import com.example.ebankify.domain.dtos.TransactionDTO;
import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.entities.Bank;
import com.example.ebankify.domain.entities.Transaction;
import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import com.example.ebankify.domain.requests.TransactionRequest;
import com.example.ebankify.exception.AccountNotFoundException;
import com.example.ebankify.exception.InsufficientFundsException;
import com.example.ebankify.exception.LimitExceededException;
import com.example.ebankify.mapper.AccountMapper;
import com.example.ebankify.mapper.TransactionMapper;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.repository.TransactionRepository;
import com.example.ebankify.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSaveTransaction_ShouldSaveTransactionAndUpdateAccounts() {

        Bank bank1 = new Bank();
        bank1.setId(1L);
        bank1.setName("Bank A");

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(1000);
        sourceAccount.setBank(bank1);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(200);
        destinationAccount.setBank(bank1);

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setSourceAccountId(1L);
        transactionRequest.setDestinationAccountId(2L);
        transactionRequest.setAmount(500);
        transactionRequest.setType(TransactionType.STANDARD);

        Transaction expectedTransaction = Transaction.builder()
                .type(TransactionType.STANDARD)
                .amount(500)
                .status(TransactionStatus.PENDING)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .build();
        AccountDTO accountSourceDTO = new AccountDTO();
        accountSourceDTO.setId(1L);
        AccountDTO accountDestinationDTO = new AccountDTO();
        accountDestinationDTO.setId(2L);
        TransactionDTO expectedTransactionDTO = new TransactionDTO();
        expectedTransactionDTO.setSourceAccount(accountSourceDTO);
        expectedTransactionDTO.setDestinationAccount(accountDestinationDTO);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedTransaction);
        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(expectedTransactionDTO);

        TransactionDTO result = transactionService.saveTransaction(transactionRequest);

        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(sourceAccount);
        verify(accountRepository, times(1)).save(destinationAccount);

        assertNotNull(result);
        assertEquals(1L, result.getSourceAccount().getId());
        assertEquals(2L, result.getDestinationAccount().getId());

        assertEquals(495, sourceAccount.getBalance());
        assertEquals(700, destinationAccount.getBalance());
    }

    @Test
    void testSaveTransaction_ShouldThrowInsufficientFundsException() {
        // Setup
        Bank bank1 = new Bank();
        bank1.setId(1L);

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(100);
        sourceAccount.setBank(bank1);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(200);
        destinationAccount.setBank(bank1);

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setSourceAccountId(1L);
        transactionRequest.setDestinationAccountId(2L);
        transactionRequest.setAmount(500);
        transactionRequest.setType(TransactionType.STANDARD);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        assertThrows(InsufficientFundsException.class, () ->
                transactionService.saveTransaction(transactionRequest)
        );
    }
    @Test
    void testSaveTransaction_ShouldThrowLimitExceededException() {
        Bank bank1 = new Bank();
        bank1.setId(1L);

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(20000);
        sourceAccount.setBank(bank1);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(200);
        destinationAccount.setBank(bank1);

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setSourceAccountId(1L);
        transactionRequest.setDestinationAccountId(2L);
        transactionRequest.setAmount(15000);
        transactionRequest.setType(TransactionType.STANDARD);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        assertThrows(LimitExceededException.class, () ->
                transactionService.saveTransaction(transactionRequest)
        );
    }
    @Test
    void testSaveTransaction_ShouldThrowAccountNotFoundException() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setSourceAccountId(999L);

        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transactionService.saveTransaction(transactionRequest)
        );
    }

    @Test
    void testSaveTransaction_CrossBankTransaction() {

        Bank bank1 = new Bank();
        bank1.setId(1L);
        bank1.setName("Bank A");

        Bank bank2 = new Bank();
        bank2.setId(2L);
        bank2.setName("Bank B");

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(1000);
        sourceAccount.setBank(bank1);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(200);
        destinationAccount.setBank(bank2);

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setSourceAccountId(1L);
        transactionRequest.setDestinationAccountId(2L);
        transactionRequest.setAmount(500);
        transactionRequest.setType(TransactionType.STANDARD);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any())).thenReturn(new Transaction());
        when(transactionMapper.toDTO(any())).thenReturn(new TransactionDTO());


        transactionService.saveTransaction(transactionRequest);

        assertEquals(492.5, sourceAccount.getBalance());
        assertEquals(700, destinationAccount.getBalance());
    }
    @Test
    void testDeleteTransaction(){
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        transactionService.deleteTransaction(1L);
        verify(transactionRepository).deleteById(1L);
    }
    @Test
    void testGetAllTransaction() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        List<TransactionDTO> transactionDTOS = Arrays.asList(new TransactionDTO(), new TransactionDTO());

        when(transactionRepository.findAll()).thenReturn(transactions);

        when(transactionMapper.toDtoList(transactions)).thenReturn(transactionDTOS);

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
    }

    @Test
    void testGetTransactionById() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        AccountDTO sourceAccountDTO = new AccountDTO();
        sourceAccountDTO.setId(1L);

        AccountDTO destinationAccountDTO = new AccountDTO();
        destinationAccountDTO.setId(2L);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSourceAccount(sourceAccountDTO);
        transactionDTO.setDestinationAccount(destinationAccountDTO);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);
        when(accountMapper.toDto(sourceAccount)).thenReturn(sourceAccountDTO);
        when(accountMapper.toDto(destinationAccount)).thenReturn(destinationAccountDTO);

        TransactionDTO result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertNotNull(result.getSourceAccount());
        assertNotNull(result.getDestinationAccount());
        assertEquals(1L, result.getSourceAccount().getId());
        assertEquals(2L, result.getDestinationAccount().getId());
    }
}