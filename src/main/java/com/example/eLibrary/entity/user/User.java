package com.example.eLibrary.entity.user;

import com.example.eLibrary.entity.book.SavedBook;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "firstName", nullable = false)
    @Size(min = 2, max = 255, message = "FirstName length must be between {min} and {max} characters")
    private String firstName;

    @NotNull
    @Column(name = "lastName", nullable = false)
    @Size(min = 2, max = 255, message = "lastName length must be between {min} and {max} characters")
    private String lastName;

    @NotNull
    @Column(name = "password", nullable = false)
    @Size(min = 8, max = 255, message = "Password must be between {min} and {max} characters")
    @Pattern(regexp = "^.*(?=.{8,})(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
    private String password;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    @Size(min = 5, max = 255, message = "Email must be between {min} and {max} characters")
    @Email(regexp = "^[^ @]+@[^ @]+\\.[^ @]+$", message = "Email must be valid")
    private String email;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDate.now();
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SavedBook> savedBooks = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.getUserRole()));
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
        return true;
    }

}
