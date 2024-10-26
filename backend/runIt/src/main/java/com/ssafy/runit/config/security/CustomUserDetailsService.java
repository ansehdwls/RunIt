package com.ssafy.runit.config.security;

import com.ssafy.runit.domain.auth.entity.User;
import com.ssafy.runit.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException(userEmail)
        );
        return mapper(user);
    }

    private UserDetails mapper(User user) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserEmail())
                .password("")
                .authorities(Collections.emptyList())
                .build();
    }
}
