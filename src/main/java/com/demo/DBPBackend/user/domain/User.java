package com.demo.DBPBackend.user.domain;

import com.demo.DBPBackend.comment.domain.Comment;
import com.demo.DBPBackend.restaurant.domain.Restaurant;
import com.demo.DBPBackend.review.domain.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @Column(length = 30, nullable = false)
    @NotBlank
    private String name;

    @Column(length = 30, nullable = false)
    @NotBlank
    private String lastname;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @Size(min = 8, max = 128)
    @NotBlank
    private String password;

    @Column(nullable = false, length = 15, unique = true)
    @NotNull
    private String phone;

    private LocalDateTime createdAt;

    private String profileImageUrl = "https://cometec-storage.s3.amazonaws.com/Default_pfp.svg.png";


    // 1. Restaurantes de los que es dueño
    @OneToMany(mappedBy = "owner")
    private List<Restaurant> ownedRestaurants;

    // 2. Restaurantes favoritos
    @ManyToMany
    @JoinTable(
            name = "user_favorite_restaurants",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private List<Restaurant> favouriteRestaurants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @ManyToMany(mappedBy = "likedBy", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Review> likedReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;


    @Transient
    private String rolePrefix = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rolePrefix + role.name()));
    }

    @Override
    public String getUsername() { return email; }

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
