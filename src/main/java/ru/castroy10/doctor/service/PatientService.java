package ru.castroy10.doctor.service;

import ru.castroy10.doctor.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> findByName(String name);

    boolean save(Patient visit);
}
