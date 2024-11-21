package com.internetbanking.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CardDto {

    private Long id;
    private Long accountId;
    private String cardNumber;
    private String cardType;
    private LocalDateTime expirationDate;
    private String cvv;
    private String balance;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime updatedAt;
}