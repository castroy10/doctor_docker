package ru.castroy10.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import ru.castroy10.doctor.model.Patient;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("from Patient p " + "where " + "   lower(concat(p.lastName, ' ', p.firstName, ' ', p.middleName)) like concat('%', :name, '%')")
    List<Patient> findByName(@Param("name") String name);
}
