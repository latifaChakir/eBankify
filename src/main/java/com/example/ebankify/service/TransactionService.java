package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.TransactionDTO;
import com.example.ebankify.domain.entities.Account;
import com.example.ebankify.domain.entities.Transaction;
import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import com.example.ebankify.domain.requests.TransactionRequest;
import com.example.ebankify.exception.AccountNotFoundException;
import com.example.ebankify.exception.InsufficientFundsException;
import com.example.ebankify.exception.LimitExceededException;
import com.example.ebankify.exception.TransactionNotFoundException;
import com.example.ebankify.mapper.AccountMapper;
import com.example.ebankify.mapper.TransactionMapper;
import com.example.ebankify.repository.AccountRepository;
import com.example.ebankify.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private AccountMapper accountMapper;

    public TransactionDTO saveTransaction(TransactionRequest transactionRequest) {
        Account sourceAccount = accountRepository.findById(transactionRequest.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Compte source introuvable"));
        Account destinationAccount = accountRepository.findById(transactionRequest.getDestinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Compte destination introuvable"));

        if (sourceAccount.getBalance() < transactionRequest.getAmount()) {
            throw new InsufficientFundsException("Fonds insuffisants sur le compte source");
        }

        double limiteVirement = 10000;
        if (transactionRequest.getAmount() > limiteVirement) {
            throw new LimitExceededException("Le montant dépasse la limite de virement autorisée de " + limiteVirement + " DH");
        }

        // Check if the transaction is cross-bank
        boolean isCrossBank = !sourceAccount.getBank().equals(destinationAccount.getBank());

        double transactionFee = calculateTransactionFee(transactionRequest.getType(), transactionRequest.getAmount(), isCrossBank);
        double totalAmount = transactionRequest.getAmount() + transactionFee;

        Transaction transaction = Transaction.builder()
                .type(transactionRequest.getType())
                .amount(transactionRequest.getAmount())
                .status(TransactionStatus.PENDING)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .build();

        // Deduct total amount (including fees) from source account
        sourceAccount.setBalance(sourceAccount.getBalance() - totalAmount);
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionRequest.getAmount());

        transactionRepository.save(transaction);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return transactionMapper.toDTO(transaction);
    }

    private double calculateTransactionFee(TransactionType type, double amount, boolean isCrossBank) {
        double baseFeeRate = (type == TransactionType.STANDARD) ? 0.01 : 0.02;
        double crossBankAdditionalFee = isCrossBank ? 0.005 : 0.0;
        double totalFeeRate = baseFeeRate + crossBankAdditionalFee;
        return amount * totalFeeRate;
    }

    public TransactionDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction introuvable"));
        TransactionDTO transactionDTO = transactionMapper.toDTO(transaction);
        transactionDTO.setSourceAccount(accountMapper.toDto(transaction.getSourceAccount()));
        transactionDTO.setDestinationAccount(accountMapper.toDto(transaction.getDestinationAccount()));

        return transactionDTO;
    }

    public TransactionDTO updateTransaction(Long transactionId, TransactionRequest transactionRequest) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction introuvable"));
        if (transactionRequest.getStatus() != null) {
            transaction.setStatus(transactionRequest.getStatus());
        }
        if (transactionRequest.getAmount() > 0) {
            Account sourceAccount = accountRepository.findById(transactionRequest.getSourceAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Compte source introuvable"));
            Account destinationAccount = accountRepository.findById(transactionRequest.getDestinationAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Compte destination introuvable"));

            boolean isCrossBank = !sourceAccount.getBank().equals(destinationAccount.getBank());

            double transactionFee = calculateTransactionFee(transaction.getType(), transactionRequest.getAmount(),isCrossBank);
            double totalAmount = transactionRequest.getAmount() + transactionFee;
            double limiteVirement = 10000;
            if (transactionRequest.getAmount() > limiteVirement) {
                throw new LimitExceededException("Le montant dépasse la limite de virement autorisée de " + limiteVirement + " DH");
            }

            if (sourceAccount.getBalance() < totalAmount) {
                throw new InsufficientFundsException("Fonds insuffisants sur le compte source pour la mise à jour");
            }

            sourceAccount.setBalance(sourceAccount.getBalance() - totalAmount);
            destinationAccount.setBalance(destinationAccount.getBalance() + transactionRequest.getAmount());

            transaction.setAmount(transactionRequest.getAmount());
            transaction.setSourceAccount(sourceAccount);
            transaction.setDestinationAccount(destinationAccount);

            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);
        }
        transactionRepository.save(transaction);
        return transactionMapper.toDTO(transaction);
    }
    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }
    public List<TransactionDTO> getAllTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionMapper.toDtoList(transactions);
    }

    public boolean cancelTransaction(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (!transactionOptional.isPresent()) {
            throw new TransactionNotFoundException("Transaction introuvable");
        }
        Transaction transaction = transactionOptional.get();
        if (transaction.getStatus() == TransactionStatus.COMPLETED) {
            throw new IllegalStateException("Impossible de annuler une transaction déjà complétée");
        }
        transaction.setStatus(TransactionStatus.REJECTED);
        transactionRepository.save(transaction);
        return true;
    }
    public boolean acceptTransaction(Long transactionId){
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (!transactionOptional.isPresent()) {
            throw new TransactionNotFoundException("Transaction introuvable");
        }
        Transaction transaction = transactionOptional.get();
        if (transaction.getStatus()!= TransactionStatus.PENDING) {
            throw new IllegalStateException("Impossible de accepter une transaction déjà validée");
        }
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        return true;
    }

    public List<TransactionDTO> getTransactionHistoryByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Compte introuvable"));
        List<Transaction> transactions = transactionRepository.findBySourceAccountOrDestinationAccount(account, account);

        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    public List<TransactionDTO> searchTransactionsByAmount(double amount) {
        List<Transaction> transactions = transactionRepository.findByAmount(amount);
        return transactionMapper.toDtoList(transactions);
    }


}
