package com.internetbanking.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AccountDto {
    private Long id;
    private Long userId;
    private String accountNumber;
    private String accountType;
    private String balance;
    private String currency;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}