package com.internetbanking.dto;

import com.internetbanking.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class AccountDto {
    private Long id;
    private Long userId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}