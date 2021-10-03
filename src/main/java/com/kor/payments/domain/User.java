package com.kor.payments.domain;

import com.kor.payments.constants.Constant;
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
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = Constant.CANNOT_BE_EMPTY)
    @Length(max = 20, message = Constant.TOO_LONG_MORE_THEN_20_CHARACTERS)
    private String lastName;
    @NotBlank(message = Constant.CANNOT_BE_EMPTY)
    @Length(max = 20, message = Constant.TOO_LONG_MORE_THEN_20_CHARACTERS)
    private String name;
    @NotBlank(message = Constant.CANNOT_BE_EMPTY)
    @Length(max = 20, message = Constant.TOO_LONG_MORE_THEN_20_CHARACTERS)
    private String middleName;
    @NotBlank(message = Constant.CANNOT_BE_EMPTY)
    private String password;
    @NotBlank(message = Constant.CANNOT_BE_EMPTY)
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

