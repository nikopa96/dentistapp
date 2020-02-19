package com.cgi.dentistapp.service;

import com.cgi.dentistapp.entity.DentistEntity;
import com.cgi.dentistapp.repository.DentistRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DentistService {

    @NonNull
    private DentistRepository dentistRepository;

    public List<DentistEntity> getAllDentists() {
        return dentistRepository.findAll();
    }
}
