package com.example.ebankify.service;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import com.example.ebankify.domain.entities.Invoice;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.InvoiceRequest;
import com.example.ebankify.exception.UserNotFoundException;
import com.example.ebankify.mapper.InvoiceMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.InvoiceRepository;
import com.example.ebankify.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {
    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;
    private UserRepository userRepository;
    private UserMapper userMapper;

    public InvoiceDTO saveInvoice(InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceMapper.toEntity(invoiceRequest);
        User user = userRepository.findById(invoiceRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        invoice.setUser(user);
        Invoice savedInvoice=invoiceRepository.save(invoice);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(savedInvoice);
        invoiceDTO.setUser(userMapper.toDto(user));
        return invoiceDTO;

    }

    public InvoiceDTO getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
               .orElseThrow(() -> new RuntimeException("Invoice not found"));
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        invoiceDTO.setUser(userMapper.toDto(invoice.getUser()));
        return invoiceDTO;
    }

    public void deleteInvoiceById(Long invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    public List<InvoiceDTO> getInvoices() {
        List<Invoice> invoices= invoiceRepository.findAll();
        return invoiceMapper.toDtoList(invoices);
    }
    public void deleteInvoices(Long id) {
        invoiceRepository.deleteById(id);
    }

}
