package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dto.*;
import com.cgi.dentistapp.entity.ClientEntity;
import com.cgi.dentistapp.entity.DentistEntity;
import com.cgi.dentistapp.entity.ProcedureEntity;
import com.cgi.dentistapp.entity.VisitEntity;
import com.cgi.dentistapp.service.ClientService;
import com.cgi.dentistapp.service.DentistService;
import com.cgi.dentistapp.service.ProcedureService;
import com.cgi.dentistapp.service.VisitService;
import com.cgi.dentistapp.utils.DTOConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
public class DentistAppRestController {

    @NonNull
    private DentistService dentistService;

    @NonNull
    private ProcedureService procedureService;

    @NonNull
    private ClientService clientService;

    @NonNull
    private VisitService visitService;

    @NonNull
    private DTOConverter dtoConverter;

    @GetMapping(path = "/getAllDentists")
    @CrossOrigin
    public List<DentistDTO> getAllDentists() {
        List<DentistEntity> dentists = dentistService.getAllDentists();

        return dentists.stream()
                .map(dentist -> (DentistDTO) dtoConverter.convertToDTO(dentist, new DentistDTO()))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/getAllProcedures")
    @CrossOrigin
    public List<ProcedureEntity> getAllProcedures() {
        return procedureService.getAllProcedures();
    }

    @GetMapping(path = "/getAllVisits")
    @CrossOrigin
    public List<VisitEntity> getAllVisits() {
        return visitService.getAllVisits();
    }

    @GetMapping(path = "/getAllClients")
    @CrossOrigin
    public List<ClientEntity> getAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping(path = "/addVisitWithClient")
    @CrossOrigin
    public VisitCreateDTO addVisitWithClient(@RequestBody @Valid VisitCreateDTO visitCreateDTO) {
        ClientEntity newClient = (ClientEntity) dtoConverter.convertToEntity(visitCreateDTO, new ClientEntity());
        VisitEntity newVisit = (VisitEntity) dtoConverter.convertToEntity(visitCreateDTO, new VisitEntity());

        ClientEntity addedClient = clientService.addClient(newClient);
        newVisit.getClient().setClientId(addedClient.getClientId());
        visitService.addVisit(newVisit);

        return null;
    }

    @GetMapping(path = "/getAllVisitsAsDTO")
    @CrossOrigin
    public List<VisitPreviewDTO> getAllVisitsAsDTO() {
        List<VisitEntity> visits = visitService.getAllVisits();

        return visits.stream()
                .map(visit -> (VisitPreviewDTO) dtoConverter.convertToDTO(visit, new VisitPreviewDTO()))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/getVisit/{visitId}")
    public VisitDetailViewDTO getVisitDetail(@PathVariable Long visitId) {
        Optional<VisitEntity> visitOptional = visitService.getVisitId(visitId);

        if (visitOptional.isPresent()) {
            VisitEntity visit = visitOptional.get();
            return (VisitDetailViewDTO) dtoConverter.convertToDTO(visit, new VisitDetailViewDTO());
        } else {
            return null;
        }
    }

//    @PutMapping(path = "/update/{visitId}")
//    public VisitUpdateDTO updateVisit(@PathVariable Long visitId, @RequestBody VisitUpdateDTO visitUpdateDTO) {
//        VisitEntity visitEntity = (VisitEntity) dtoConverter.convertToEntity(visitUpdateDTO, new VisitEntity());
//        VisitEntity updatedVisit = visitService.updateVisit(visitId, visitEntity);
//
//        if (updatedVisit != null) {
//            return (VisitUpdateDTO) dtoConverter.convertToDTO(updatedVisit, new VisitUpdateDTO());
//        } else {
//            return null;
//        }
//    }

    @GetMapping(path = "/getVisitsByDentistId")
    public List<VisitEntity> getVisitsByDentistId() throws ParseException {
        Date dateExample = new SimpleDateFormat("dd.MM.yyyy").parse("20.02.2020");
        Date timeExample = new SimpleDateFormat("HH:mm").parse("18:00");

        return visitService.getVisitsByDentistIdAndDateAndTime(7, dateExample, timeExample);
    }

    @GetMapping(path = "/getVisitsByDentistId/{request}")
    public List<VisitEntity> searchVisitByDentistOrClient(@PathVariable String request) {
        return visitService.getAllVisitsBySearchRequest(request);
    }
}
