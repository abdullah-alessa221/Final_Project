package com.example.finalprojectjavabootcamp.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NotEmpty(message = "Name is required")
//    @Column(columnDefinition = "varchar(100) not null")
    private String name;
//


//    @Email(message = "Email should be in email format")
//    @NotEmpty(message = "Email is required")
//    @Column(columnDefinition = "varchar(100) not null")
    private String email;

    //    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits")
//    @NotEmpty(message = "Phone number is required")
//    @Column(columnDefinition = "varchar(10) not null")
    private String phone;


//    @NotEmpty(message = "Location is required")
//    @Column(columnDefinition = "varchar(50) not null)")
    private String location;


//    @NotEmpty(message = "Password is required")
//    @Column(columnDefinition = "varchar(250) not null")
    private String password;

//    @NotEmpty(message = "Confirm password is required")
//    @Column(columnDefinition = "varchar(250) not null")
    private String confirmPassword;


//    @Pattern(regexp = "SELLER|BUYER|ADMIN", message = "Role must be SELLER, BUYER, or ADMIN")
    private String role;

    private String status;

@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
@PrimaryKeyJoinColumn
private Buyer buyer;

@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
@PrimaryKeyJoinColumn
private Seller seller;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
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
