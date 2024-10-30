package com.example.ebankify.domain.entities;

import jakarta.persistence.*;
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
    private double amountDue;
    @Column(name = "due_date")
    private Date dueDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
