package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import com.example.ebankify.domain.entities.Invoice;
import com.example.ebankify.domain.requests.InvoiceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceMapper Instance = Mappers.getMapper(InvoiceMapper.class);
    InvoiceDTO toDto(Invoice invoice);
    Invoice toEntity(InvoiceDTO invoiceDto);
    Invoice toEntity(InvoiceRequest invoiceRequest);
    List<InvoiceDTO> toDtoList(List<Invoice> invoices);
}
