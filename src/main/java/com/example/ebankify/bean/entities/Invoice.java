package com.example.ebankify.bean.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "invoices")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0, message = "Le montant dû ne peut pas être négatif")
    private double amountDue;
    @Column(name = "due_date")
    private Date dueDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
