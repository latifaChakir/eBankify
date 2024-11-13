package com.example.ebankify;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import com.example.ebankify.domain.entities.Invoice;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.InvoiceRequest;
import com.example.ebankify.exception.UserNotFoundException;
import com.example.ebankify.mapper.InvoiceMapper;
import com.example.ebankify.mapper.UserMapper;
import com.example.ebankify.repository.InvoiceRepository;
import com.example.ebankify.repository.UserRepository;
import com.example.ebankify.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInvoiceById() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        User user = new User();
        invoice.setUser(user);
        InvoiceDTO invoiceDTO = new InvoiceDTO();

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(invoiceDTO);
        when(userMapper.toDto(user)).thenReturn(invoiceDTO.getUser());

        InvoiceDTO result = invoiceService.getInvoiceById(1L);

        assertNotNull(result);
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceMapper, times(1)).toDto(invoice);
    }

    @Test
    void testGetInvoiceById_NotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> invoiceService.getInvoiceById(1L));
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteInvoiceById() {
        doNothing().when(invoiceRepository).deleteById(1L);

        invoiceService.deleteInvoiceById(1L);

        verify(invoiceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetInvoices() {
        List<Invoice> invoices = Arrays.asList(new Invoice(), new Invoice());
        when(invoiceRepository.findAll()).thenReturn(invoices);
        when(invoiceMapper.toDtoList(invoices)).thenReturn(Arrays.asList(new InvoiceDTO(), new InvoiceDTO()));

        List<InvoiceDTO> result = invoiceService.getInvoices();

        assertEquals(2, result.size());
        verify(invoiceRepository, times(1)).findAll();
        verify(invoiceMapper, times(1)).toDtoList(invoices);
    }

    @Test
    void testSaveInvoice() {
        Invoice invoice = new Invoice();
        User user = new User();
        user.setId(1L);
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(invoiceMapper.toEntity(invoiceRequest)).thenReturn(invoice);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDto(invoice)).thenReturn(invoiceDTO);
        when(userMapper.toDto(user)).thenReturn(invoiceDTO.getUser());

        InvoiceDTO result = invoiceService.saveInvoice(invoiceRequest);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(invoice);
        verify(invoiceMapper, times(1)).toDto(invoice);
    }

    @Test
    void testSaveInvoice_UserNotFound() {
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> invoiceService.saveInvoice(invoiceRequest));
        verify(userRepository, times(1)).findById(1L);
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }
}
