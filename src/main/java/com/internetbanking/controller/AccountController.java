package com.internetbanking.controller;

import com.internetbanking.dto.AccountDto;
import com.internetbanking.entity.Account;
import com.internetbanking.entity.User;
import com.internetbanking.service.AccountService;
import com.internetbanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService, UserService userService) {
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
        return "account";
    }

    @GetMapping("/add")
    public String showAddAccountForm(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "add_account";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute Account account) {
        User currentUser = getCurrentUser();
        String generatedAccountNumber = generateAccountNumber();
        account.setAccountNumber(generatedAccountNumber);
        account.setStatus("Active");
        account.setUser(currentUser);
        accountService.save(account);
        return "redirect:/accounts";
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(16);
        for (int i = 0; i < 20; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
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
