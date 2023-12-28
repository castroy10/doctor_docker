package ru.castroy10.doctor.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.castroy10.doctor.model.Doctor;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @EntityGraph(attributePaths = {"category"})
    List<Doctor> findAll();

    @EntityGraph(attributePaths = {"category"})
    @Query("from Doctor d " + "where " + "   lower(concat(d.lastName, ' ', d.firstName, ' ', d.middleName)) like concat('%', :name, '%')")
    List<Doctor> findByName(@Param("name") String name);
}
