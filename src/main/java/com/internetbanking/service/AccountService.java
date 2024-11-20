package com.internetbanking.service;

import com.internetbanking.entity.Account;
import com.internetbanking.entity.User;
import com.internetbanking.exception.NotFoundException;
import com.internetbanking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save(Account Account) {
        return accountRepository.save(Account);
    }

    public void delete(Account Account) {
        accountRepository.delete(Account);
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public Optional<Account> updateAccount(Long id, Account updatedAccount) {
        return accountRepository.findById(id)
                .map(existingAccount -> {
                    existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
                    existingAccount.setBalance(updatedAccount.getBalance());
                    existingAccount.setAccountType(updatedAccount.getAccountType());
                    existingAccount.setCurrency(updatedAccount.getCurrency());
                    existingAccount.setStatus(updatedAccount.getStatus());
                    existingAccount.setUpdatedAt(updatedAccount.getUpdatedAt());
                    return accountRepository.save(existingAccount);
                });
    }

    @Transactional
    public void deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
            throw new NotFoundException("Account not found with id: " + id);
        }
    }

    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountsByUser(User user) {
        return accountRepository.findByUser(user);  // Ensure this returns List<Account>
    }
}
