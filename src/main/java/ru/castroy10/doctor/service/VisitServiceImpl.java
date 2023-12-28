package ru.castroy10.doctor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private static final Logger log = LoggerFactory.getLogger(VisitServiceImpl.class);

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Transactional
    public boolean save(Visit visit) {
        Assert.notNull(visit, "Visit must not be null");
        try {
            visitRepository.save(visit);
            return true;
        } catch (RuntimeException e) {
            log.error("Запись визита не удалась " + e);
            return false;
        }
    }

    @Transactional
    public List<Visit> findVisitIsNotFinishedForDoctor(Doctor doctor) {
        Assert.notNull(doctor, "Doctor must not be null");
        try {
            return visitRepository.findByIsFinishedFalse(doctor.getId());
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<Visit> findByName(String name) {
        try {
            return visitRepository.findByName(name);
        } catch (RuntimeException e) {
            log.error("Чтение визита не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public Visit findById(Long id) {
        try {
            Optional<Visit> visitOptional = visitRepository.findById(id);
            if (visitOptional.isEmpty()) throw new NoSuchElementException("Visit not found");
            return visitOptional.get();
        } catch (RuntimeException e) {
            log.error("Чтение визита не удалось " + e);
            return new Visit();
        }
    }

    @Transactional
    public List<Visit> findAllVisitForPatient(Patient patient) {
        try {
            return visitRepository.findAllForPatient(patient.getId());
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<Visit> findAllVisitForPatient(Doctor doctor) {
        try {
            return visitRepository.findAllForDoctor(doctor.getId());
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<Visit> findAllVisitForPatient(Patient patient, Doctor doctor) {
        try {
            return visitRepository.findAllForPatientAndDoctor(patient.getId(), doctor.getId());
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<Visit> findAllVisitForPatient(Patient patient, LocalDateTime dateAfter, LocalDateTime dateBefore) {
        try {
            return visitRepository.findAllForPatientWithDate(patient.getId(), dateAfter, dateBefore);
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<Visit> findAllVisitForPatient(Doctor doctor, LocalDateTime dateAfter, LocalDateTime dateBefore) {
        try {
            return visitRepository.findAllForDoctorWithDate(doctor.getId(), dateAfter, dateBefore);
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<Visit> findAllVisitForPatient(Patient patient, Doctor doctor, LocalDateTime dateAfter, LocalDateTime dateBefore) {
        try {
            return visitRepository.findAllForPatientAndDoctorAndDate(patient.getId(), doctor.getId(), dateAfter, dateBefore);
        } catch (RuntimeException e) {
            log.error("Чтение визитов не удалось " + e);
            return Collections.emptyList();
        }
    }
}