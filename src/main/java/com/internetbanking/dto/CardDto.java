package com.internetbanking.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class CardDto {

    private Long id;
    private String cardNumber;
    private String cardType;
    private LocalDateTime expirationDate;
    private String cvv;
    private BigDecimal balance;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime updatedAt;
}