package com.internetbanking.controller;

import com.internetbanking.dto.CardDto;
import com.internetbanking.entity.Account;
import com.internetbanking.entity.Card;
import com.internetbanking.entity.User;
import com.internetbanking.service.AccountService;
import com.internetbanking.service.CardService;
import com.internetbanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

        // Get list of accounts for the current user
        List<Account> accounts = accountService.findAccountsByUser(currentUser);

        // Get cards for the accounts
        List<Card> cards = cardService.findCardsByAccounts(accounts);

        // Convert cards to DTOs if necessary
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
    public String showAddCard(Model model) {
        model.addAttribute("card", new Card());
        return "add-card";
    }

    @PostMapping("/add")
    public String addCard(@ModelAttribute Card card) {
        cardService.createCard(card);
        return "redirect:/cards";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card updatedCard) {
        return cardService.updateCard(id, updatedCard)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        if (cardService.deleteCard(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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
            return "errorPage"; // Page to show error if card not found
        }
        Card fromCard = fromCardOpt.get();
        Card toCard = toCardOpt.get();
        try {
            transactionService.transferFunds(fromCard.getId(), toCard.getId(), amount);
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
        cardDto.setBalance(card.getBalance());
        cardDto.setStatus(card.getStatus());
        cardDto.setIssuedAt(card.getIssuedAt());
        cardDto.setUpdatedAt(card.getUpdatedAt());
        return cardDto;

    }
}
