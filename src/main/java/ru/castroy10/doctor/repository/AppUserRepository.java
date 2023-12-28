package ru.castroy10.doctor.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.castroy10.doctor.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @EntityGraph(attributePaths = {"roles", "doctor"})
    Optional<AppUser> findAppUserByUsername(String username);
}
