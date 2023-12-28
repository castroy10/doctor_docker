package ru.castroy10.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.castroy10.doctor.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
