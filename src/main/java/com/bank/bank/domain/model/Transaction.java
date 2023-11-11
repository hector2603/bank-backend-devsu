package com.bank.bank.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal value;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}