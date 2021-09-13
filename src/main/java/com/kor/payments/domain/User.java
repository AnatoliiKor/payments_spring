package com.kor.payments.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "usr")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Length(max = 20, message = "Too long (more then 20 chracters)")
    private String lastName;
    @NotBlank
    @Length(max = 20, message = "Too long (more then 20 chracters)")
    private String name;
    @Length(max = 20, message = "Too long (more then 20 chracters)")
    private String middleName;
    @NotBlank
    private String password;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank
    @Column(unique = true)
    private long phoneNumber;
    @NotBlank
    private LocalDateTime registered = LocalDateTime.now();
    @NotBlank
    private boolean active;
//    @NotBlank
//    @Enumerated(EnumType.STRING)
//    @OneToOne
//    @JoinColumn(name = "role_id", referencedColumnName = "id")
//    private Role role;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Account> accountNumbers = new ArrayList<>();

    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Account> getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(List<Account> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }

    public String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return this.registered.format(formatter);
    }
}

