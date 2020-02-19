package com.cgi.dentistapp.service;

import com.cgi.dentistapp.entity.ProcedureEntity;
import com.cgi.dentistapp.repository.ProcedureRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProcedureService {

    @NonNull
    private ProcedureRepository procedureRepository;

    public List<ProcedureEntity> getAllProcedures() {
        return procedureRepository.findAll();
    }
}
