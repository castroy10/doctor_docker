package ru.castroy10.doctor.service;

import org.springframework.transaction.annotation.Transactional;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    boolean save(Visit visit);

    Visit findById(Long id);

    List<Visit> findByName(String name);

    List<Visit> findVisitIsNotFinishedForDoctor(Doctor doctor);

    List<Visit> findAllVisitForPatient(Patient patient);

    List<Visit> findAllVisitForPatient(Doctor doctor);

    List<Visit> findAllVisitForPatient(Patient patient, Doctor doctor);

    List<Visit> findAllVisitForPatient(Patient patient, LocalDateTime dateAfter, LocalDateTime dateBefore);

    List<Visit> findAllVisitForPatient(Doctor doctor, LocalDateTime dateAfter, LocalDateTime dateBefore);

    List<Visit> findAllVisitForPatient(Patient patient, Doctor doctor, LocalDateTime dateAfter, LocalDateTime dateBefore);
}
