package ru.castroy10.doctor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.castroy10.doctor.enumer.RoleName;


@SpringBootTest
class AppUserServiceTest {

    private final AppUserService appUserService;

    @Autowired
    AppUserServiceTest(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Test
    void checkRoleForCurrentUser() {
        UserDetails fakeUser = User.withUsername("appuser").password("12345").roles("READER").build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(fakeUser, fakeUser.getPassword(), fakeUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Assertions.assertTrue(appUserService.checkRoleForCurrentUser(RoleName.ROLE_READER));
    }
}