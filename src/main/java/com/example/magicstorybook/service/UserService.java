package com.example.magicstorybook.service;


import com.example.magicstorybook.model.User;
import com.example.magicstorybook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    public Optional<User> findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        logger.debug("User found: {}", user);
        return user;
    }


}

