package com.cgi.dentistapp.service;

import com.cgi.dentistapp.entity.VisitEntity;
import com.cgi.dentistapp.repository.VisitRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitService {

    @NonNull
    private VisitRepository visitRepository;

    public List<VisitEntity> getAllVisits() {
        return visitRepository.findAll();
    }

    public Optional<VisitEntity> getVisitId(Long visitId) {
        return visitRepository.findById(visitId);
    }

    public List<VisitEntity> getVisitsByDentistIdAndDateAndTime(Integer dentistId, Date date, Date time) {
        return visitRepository.findAllByDentistIdAndDateAndTime(dentistId, date, time);
    }

    public List<VisitEntity> getAllVisitsBySearchRequest(String request) {
        return visitRepository.findAllBySearchRequest(request);
    }

    public VisitEntity addVisit(VisitEntity visit) {
        return visitRepository.save(visit);
    }

    public void updateVisit(Long visitId, VisitEntity newVisit) {
        Optional<VisitEntity> requestedVisit = visitRepository.findById(visitId);

        if (requestedVisit.isPresent()) {
            VisitEntity visit = requestedVisit.get();

            visit.setDate(newVisit.getDate());
            visit.setTime(newVisit.getTime());
            visit.setDentist(newVisit.getDentist());
            visit.setProcedure(newVisit.getProcedure());

            visitRepository.save(visit);
        }
    }

    public void deleteVisit(Long visitId) {
        Optional<VisitEntity> requestedVisit = visitRepository.findById(visitId);

        if (requestedVisit.isPresent()) {
            visitRepository.deleteById(visitId);
        }
    }
}
