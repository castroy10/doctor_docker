package ru.castroy10.doctor.service;

import ru.castroy10.doctor.model.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> findAll();

    List<Doctor> findByName(String name);
}
