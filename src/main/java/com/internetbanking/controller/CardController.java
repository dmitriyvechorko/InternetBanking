package com.internetbanking.controller;

import com.internetbanking.dto.CardDto;
import com.internetbanking.entity.Account;
import com.internetbanking.entity.Card;
import com.internetbanking.entity.User;
import com.internetbanking.service.AccountService;
import com.internetbanking.service.CardService;
import com.internetbanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
    private final TransactionService transactionService;
    private final AccountService accountService;

    @Autowired
    public CardController(CardService cardService, TransactionService transactionService, AccountService accountService) {
        this.cardService = cardService;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @GetMapping("")
    public String listCards(Model model) {

        User currentUser = getCurrentUser();

        List<Account> accounts = accountService.findAccountsByUser(currentUser);

        List<Card> cards = cardService.findCardsByAccounts(accounts);

        List<CardDto> cardDtos = cards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        model.addAttribute("cardList", cardDtos);
        return "cards";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("{id}")
    public String showCard(@PathVariable Long id, Model model) {
        Optional<Card> card = cardService.findCardById(id);
        card.ifPresent(value -> model.addAttribute("card", convertToDto(value)));
        return "card";
    }

    @GetMapping("/add")
    public String showAddCardForm(Model model) {
        User currentUser = getCurrentUser();
        List<Account> accounts = accountService.findAccountsByUser(currentUser);
        model.addAttribute("accounts", accounts);
        model.addAttribute("card", new CardDto());
        return "add_card";
    }

    @PostMapping("/add")
    public String addCard(@ModelAttribute("card") Card card, @RequestParam("accountId") Long accountId) {
        Account account = accountService.findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
        String generatedCardNumber = generateCardNumber();
        String generatedCvv = generateCvv();
        LocalDateTime expirationDate = calculateExpirationDate();

        card.setCardNumber(generatedCardNumber);
        card.setCvv(generatedCvv);

        card.setExpirationDate(expirationDate);
        card.setAccount(account);
        cardService.save(card);
        return "redirect:/cards";
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }

    private String generateCvv() {
        Random random = new Random();
        int cvv = random.nextInt(900) + 100; // Генерируем случайное число от 100 до 999
        return String.valueOf(cvv);
    }

    private LocalDateTime calculateExpirationDate() {
        return LocalDateTime.now().plusYears(4); //
    }

    @PostMapping("/transfer")
    public String transferFunds(@RequestParam("fromCard") String fromCardNumber,
                                @RequestParam("toCard") String toCardNumber,
                                @RequestParam("amount") BigDecimal amount,
                                Model model) {

        Optional<Card> fromCardOpt = cardService.findByCardNumber(fromCardNumber);
        Optional<Card> toCardOpt = cardService.findByCardNumber(toCardNumber);

        if (!fromCardOpt.isPresent() || !toCardOpt.isPresent()) {
            model.addAttribute("error", "Одна из карт не найдена");
            return "errorPage";
        }
        Card fromCard = fromCardOpt.get();
        Card toCard = toCardOpt.get();
        try {
            transactionService.transferCardFunds(fromCard.getId(), toCard.getId(), amount);
            model.addAttribute("success", "Перевод успешен");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при переводе: " + e.getMessage());
        }

        return "redirect:/cards";
    }

    private CardDto convertToDto(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setCardType(card.getCardType());
        cardDto.setExpirationDate(card.getExpirationDate());
        cardDto.setCvv(card.getCvv());
        cardDto.setBalance(String.valueOf(card.getBalance()));
        cardDto.setStatus(String.valueOf(card.getStatus()));
        cardDto.setIssuedAt(card.getIssuedAt());
        cardDto.setUpdatedAt(card.getUpdatedAt());
        if (card.getAccount() != null) {
            cardDto.setAccountId(card.getAccount().getId());
        }
        return cardDto;

    }
}
