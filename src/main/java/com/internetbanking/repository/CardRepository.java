package com.internetbanking.repository;

import com.internetbanking.entity.Account;
import com.internetbanking.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cvv);

    List<Card> findByAccountIn(List<Account> accounts);
}
