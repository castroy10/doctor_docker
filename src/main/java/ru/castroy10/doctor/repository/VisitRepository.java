package ru.castroy10.doctor.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.castroy10.doctor.model.Visit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.isFinished = false and v.doctor.id=:id")
    List<Visit> findByIsFinishedFalse(@Param("id") Long id);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + "   lower(concat(v.patient.lastName, ' ', v.patient.firstName, ' ', v.patient.middleName)) like concat('%', :name, '%')")
    List<Visit> findByName(@Param("name") String name);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.patient.id=:id")
    List<Visit> findAllForPatient(@Param("id") Long id);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.doctor.id=:id")
    List<Visit> findAllForDoctor(@Param("id") Long id);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.patient.id=:idPatient and v.doctor.id=:idDoctor")
    List<Visit> findAllForPatientAndDoctor(@Param("idPatient") Long idPatient, @Param("idDoctor") Long idDoctor);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.patient.id=:id and v.dateTime between :dateAfter and :dateBefore")
    List<Visit> findAllForPatientWithDate(@Param("id") Long id, @Param("dateAfter") LocalDateTime dateAfter, @Param("dateBefore") LocalDateTime dateBefore);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.doctor.id=:id and v.dateTime between :dateAfter and :dateBefore")
    List<Visit> findAllForDoctorWithDate(@Param("id") Long id, @Param("dateAfter") LocalDateTime dateAfter, @Param("dateBefore") LocalDateTime dateBefore);

    @EntityGraph(attributePaths = {"patient", "doctor", "doctor.category"})
    @Query("from Visit v " + "where " + " v.patient.id=:idPatient and v.doctor.id=:idDoctor " + " and v.dateTime between :dateAfter and :dateBefore")
    List<Visit> findAllForPatientAndDoctorAndDate(@Param("idPatient") Long idPatient, @Param("idDoctor") Long idDoctor, @Param("dateAfter") LocalDateTime dateAfter, @Param("dateBefore") LocalDateTime datebefore);

}
