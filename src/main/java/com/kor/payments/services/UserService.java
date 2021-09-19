package com.kor.payments.services;

import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{
    private final Logger log = LogManager.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    public boolean addUser(User user){
        if(findUserByEmail(user.getEmail()) !=null) {
            return false;
        }
        user.setActive(true);
        user.setRole(Role.CLIENT);
        userRepository.save(user);
        return true;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }

//        return user;
//    }

    public User findById (Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void setEmail(User user) {
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public boolean setIsActive(User user, boolean isActive) {
        user.setActive(isActive);
        if (userRepository.save(user).isActive() == isActive) {
                log.info("Action is changed to {} for user {}", isActive, user.getEmail());
                return true;
        }
            log.info("Action is not changed to {} for user {}", isActive, user.getEmail());
            return false;
        }

    public List<User> findAllPage(int page, String sort, String order) {
        return userRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.valueOf(order), sort))).getContent();
    }
}

