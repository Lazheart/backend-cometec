package com.demo.DBPBackend.conf;

import com.demo.DBPBackend.exceptions.ResourceNotFoundException;
import com.demo.DBPBackend.user.domain.User;
import com.demo.DBPBackend.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConf {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                return (UserDetails) user;
            } catch (Exception ex) {
                return org.springframework.security.core.userdetails.User
                        .withUsername("dummy")
                        .password("dummy")
                        .roles("DUMMY")
                        .build();
            }
        };
    }
}
