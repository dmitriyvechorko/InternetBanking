package com.internetbanking.service;

import com.internetbanking.entity.Account;
import com.internetbanking.entity.Card;
import com.internetbanking.entity.User;
import com.internetbanking.repository.AccountRepository;
import com.internetbanking.repository.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    public CardService(CardRepository cardRepository,
                       TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> findCardById(Long id) {
        return cardRepository.findById(id);
    }

    public void save(Card card) {
        LocalDateTime now = LocalDateTime.now();
        card.setIssuedAt(now);
        card.setUpdatedAt(now);
        card.setExpirationDate(now.plusYears(4));
        Account account = card.getAccount();
        if (account != null) {
            card.setBalance(account.getBalance());
        }

        cardRepository.save(card);
    }

    @Transactional
    public void createCard(Card card) {
        cardRepository.save(card);
    }

    @Transactional
    public Optional<Card> updateCard(Long id, Card updatedCard) {
        return cardRepository.findById(id).map(existingCard -> {
            existingCard.setCardNumber(updatedCard.getCardNumber());
            existingCard.setExpirationDate(updatedCard.getExpirationDate());
            existingCard.setCvv(updatedCard.getCvv());
            existingCard.setCardType(updatedCard.getCardType());
            existingCard.setStatus(updatedCard.getStatus());
            existingCard.setUpdatedAt(updatedCard.getUpdatedAt());
            return cardRepository.save(existingCard);
        });
    }


    @Transactional
    public boolean deleteCard(Long id) {
        if (cardRepository.existsById(id)) {
            cardRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Card> findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    public List<Account> findAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    public List<Card> findCardsByAccounts(List<Account> accounts) {
        return cardRepository.findByAccountIn(accounts);
    }
}
