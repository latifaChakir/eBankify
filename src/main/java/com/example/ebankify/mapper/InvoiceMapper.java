package com.example.ebankify.mapper;

import com.example.ebankify.domain.dtos.InvoiceDTO;
import com.example.ebankify.domain.entities.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceMapper Instance = Mappers.getMapper(InvoiceMapper.class);
    InvoiceDTO toDto(Invoice invoice);
    Invoice toEntity(InvoiceDTO invoiceDto);
}
