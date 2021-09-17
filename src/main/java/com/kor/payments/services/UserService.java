package com.kor.payments.services;

import com.kor.payments.domain.Role;
import com.kor.payments.domain.User;
import com.kor.payments.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    public boolean addUser(User user){
        if(userRepository.findByEmail(user.getEmail()) !=null) {
            return false;
        }
        user.setActive(true);
        user.setRole(Role.CLIENT);
        userRepository.save(user);
        return true;
    }



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }

//        return user;
//    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById (Long id) {
        return userRepository.findById(id).orElse(null);
    }
//    public User findByUsername (String username) {
//        return userRepository.findByUsername(username);

//    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
//    public void save(User user, Map<String, String> form) {
//        Set<String> roles = Arrays.stream(Role.values())
//                .map(Role::name)
//                .collect(Collectors.toSet());
//        user.getRoles().clear();
//        user.setActive(false);
//        for (String key : form.keySet()) {
//            if (key.equals("active")) user.setActive(true);
//            if(roles.contains(key)) {
//                user.getRoles().add(Role.valueOf(key));
//            }
//        }
//        userRepository.save(user);

//    }

//    public void setPassword(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }

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
}

