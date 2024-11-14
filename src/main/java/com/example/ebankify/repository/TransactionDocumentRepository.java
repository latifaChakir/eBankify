package com.example.ebankify.repository;

import com.example.ebankify.domain.elasticsearch.TransactionDocument;
import com.example.ebankify.domain.enums.TransactionStatus;
import com.example.ebankify.domain.enums.TransactionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TransactionDocumentRepository extends ElasticsearchRepository<TransactionDocument, String> {
    List<TransactionDocument> findByAmount(double amount);
    List<TransactionDocument> findByType(TransactionType type);
    List<TransactionDocument> findByStatus(TransactionStatus status);
}
