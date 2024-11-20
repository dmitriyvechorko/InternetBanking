package com.internetbanking.controller;

import com.internetbanking.dto.AccountDto;
import com.internetbanking.dto.CardDto;
import com.internetbanking.entity.Account;
import com.internetbanking.entity.Card;
import com.internetbanking.entity.User;
import com.internetbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    public String listAccounts(Model model) {
        User currentUser = getCurrentUser();
        List<Account> accounts = accountService.findAccountsByUser(currentUser);
        List<AccountDto> accountDtos = accounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        model.addAttribute("accountList", accountDtos);
        return "accounts";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("{id}")
    public String showAccount(@PathVariable Long id, Model model) {
        Optional<Account> account = accountService.findAccountById(id);
        account.ifPresent(value -> model.addAttribute("account", convertToDto(value)));
        return "acccount";
    }

    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setStatus(account.getStatus());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }
}
