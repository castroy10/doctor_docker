package ru.castroy10.doctor.service;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.castroy10.doctor.enumer.RoleName;
import ru.castroy10.doctor.model.AppUser;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.repository.AppUserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Doctor getDoctorForLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(authentication.getName()).get();
        Hibernate.initialize(appUser.getDoctor().getCategory());
        return appUser.getDoctor();
    }

    public Set<String> getRolesForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public boolean checkRoleForCurrentUser(RoleName roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals(roleName.name()));
    }
}
