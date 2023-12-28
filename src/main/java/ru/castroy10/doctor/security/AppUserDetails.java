package ru.castroy10.doctor.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.castroy10.doctor.model.AppUser;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AppUserDetails implements UserDetails {

    private final AppUser appUser;

    public AppUserDetails(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : appUser.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.appUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.appUser.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.appUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.appUser.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.appUser.isEnabled();
    }

    public Doctor getDoctor() {
        return this.appUser.getDoctor();
    }
}
