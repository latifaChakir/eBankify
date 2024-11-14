package com.example.ebankify.domain.elasticsearch;

import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;

@Data
@Document(indexName = "transactions")
public class TransactionDocument {
    @Id
    private String id;
    private TransactionType type;
    private double amount;
    private TransactionStatus status;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private LocalDate nextExecutionDate;
    private LocalDate createdAt;
}
