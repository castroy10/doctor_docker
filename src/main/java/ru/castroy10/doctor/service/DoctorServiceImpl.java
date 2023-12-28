package ru.castroy10.doctor.service;

import org.springframework.stereotype.Service;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> findByName(String name) {
        return doctorRepository.findByName(name);
    }
}
