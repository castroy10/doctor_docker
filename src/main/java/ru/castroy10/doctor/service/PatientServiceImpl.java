package ru.castroy10.doctor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.repository.PatientRepository;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findByName(String name) {
        return patientRepository.findByName(name);
    }

    @Override
    public boolean save(Patient patient) {
        Assert.notNull(patient, "Patient must not be null");
        try {
            patientRepository.save(patient);
            return true;
        } catch (RuntimeException e) {
            log.error("Запись пациента не удалась " + e);
            return false;
        }
    }
}