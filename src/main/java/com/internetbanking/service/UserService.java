package com.internetbanking.service;

import com.internetbanking.entity.Account;
import com.internetbanking.entity.User;
import com.internetbanking.exception.NotFoundException;
import com.internetbanking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User update(User user) {
        User existinguser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not Found"));

        existinguser.setUsername(user.getUsername());
        existinguser.setPassword(user.getPassword());
        existinguser.setEmail(user.getEmail());
        existinguser.setName(user.getName());
        existinguser.setSurname(user.getSurname());
        existinguser.setPatronymic(user.getPatronymic());
        existinguser.setEmail(user.getEmail());
        existinguser.setMobilePhone(user.getMobilePhone());

        return userRepository.save(existinguser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new UsernameNotFoundException(username);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
               .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}