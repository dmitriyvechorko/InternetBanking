package com.internetbanking.service;

import com.internetbanking.entity.Account;
import com.internetbanking.entity.Card;
import com.internetbanking.entity.Transaction;
import com.internetbanking.repository.AccountRepository;
import com.internetbanking.repository.CardRepository;
import com.internetbanking.repository.TransactionRepository;
import com.internetbanking.enums.TransactionType;
import com.internetbanking.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class TransactionService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public void transferFunds(Long fromCardId, Long toCardId, BigDecimal amount) {
        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        Account fromAccount = fromCard.getAccount();
        Account toAccount = toCard.getAccount();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionDate(OffsetDateTime.now());

        transactionRepository.save(transaction);
    }
}
