package com.kor.payments.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Cannot be empty")
    @Length(max = 20, message = "Too long (more then 20 chracters)")
    private String lastName;
    @NotBlank(message = "Cannot be empty")
    @Length(max = 20, message = "Too long (more then 20 chracters)")
    private String name;
    @Length(max = 20, message = "Too long (more then 20 chracters)")
    private String middleName;
    @NotBlank(message = "Cannot be empty")
    private String password;
    @NotBlank(message = "Cannot be empty")
    @Email(message = "Email is not correct")
    @Column(unique = true)
    private String email;
    @Positive(message = "Must be positive")
    @Column(unique = true)
    private long phoneNumber;
    private LocalDateTime registered = LocalDateTime.now();
    private boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    public String getFormatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return this.registered.format(formatter);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}

