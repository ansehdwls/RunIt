package com.ssafy.runit.config.security;

import com.ssafy.runit.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String userName;
    private String profileImageUrl;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getUserEmail();
        this.userName = user.getUserName();
        this.profileImageUrl = user.getImageUrl();
        this.authorities = Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }

}
