package com.library.auth;

import com.library.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class UserDetailsImpl extends User implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(long userId, String login, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userId, login, password);
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
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
