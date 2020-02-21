package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dto.*;
import com.cgi.dentistapp.entity.ClientEntity;
import com.cgi.dentistapp.entity.VisitEntity;
import com.cgi.dentistapp.service.ClientService;
import com.cgi.dentistapp.service.DentistService;
import com.cgi.dentistapp.service.ProcedureService;
import com.cgi.dentistapp.service.VisitService;
import com.cgi.dentistapp.utils.DTOConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
@RequiredArgsConstructor
public class DentistAppController extends WebMvcConfigurerAdapter {

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

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @GetMapping("/")
    public String showRegisterForm(@ModelAttribute VisitOptionsDTO visitOptions, VisitCreateDTO visitCreateDTO) {
        return "form";
    }

    @PostMapping("/")
    public String postRegisterForm(@ModelAttribute VisitOptionsDTO visitOptionsDTO, @Valid VisitCreateDTO visitCreateDTO,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        List<VisitEntity> visitEntities = visitService.getVisitsByDentistIdAndDateAndTime(visitCreateDTO.getDentistId(),
                visitCreateDTO.getDate(), visitCreateDTO.getTime());

        if (!visitEntities.isEmpty()) {
            bindingResult.addError(new FieldError("visitCreateDTO", "dentistId",
                    "registration.visitExists.error"));
            return "form";
        }

        ClientEntity newClient = (ClientEntity) dtoConverter.convertToEntity(visitCreateDTO, new ClientEntity());
        VisitEntity newVisit = (VisitEntity) dtoConverter.convertToEntity(visitCreateDTO, new VisitEntity());

        ClientEntity addedClient = clientService.addClient(newClient);
        newVisit.getClient().setClientId(addedClient.getClientId());
        VisitEntity addedVisit = visitService.addVisit(newVisit);

        redirectAttributes.addFlashAttribute("addedSuccessMessage", "visit.added.successMessage");

        return "redirect:/visit/" + addedVisit.getVisitId();
    }

    @GetMapping("/visits")
    public String showAllVisitsPage(Model model) {
        List<VisitEntity> visits = visitService.getAllVisits();

        List<VisitPreviewDTO> visitPreviewDTOs = visits.stream()
                .map(visit -> (VisitPreviewDTO) dtoConverter.convertToDTO(visit, new VisitPreviewDTO()))
                .collect(Collectors.toList());

        model.addAttribute("visitsPreviewDTOs", visitPreviewDTOs);

        return "visits";
    }

    @GetMapping("/visits/search")
    public String searchVisit(@RequestParam(value = "request", required = false) String request, Model model) {
        List<VisitEntity> foundVisits = visitService.getAllVisitsBySearchRequest(request.toLowerCase());

        List<VisitPreviewDTO> foundVisitPreviews = foundVisits.stream()
                .map(visit -> (VisitPreviewDTO) dtoConverter.convertToDTO(visit, new VisitPreviewDTO()))
                .collect(Collectors.toList());

        model.addAttribute("visitsPreviewDTOs", foundVisitPreviews);

        return "visits";
    }

    @GetMapping("/visit/{visitId}")
    public String showVisitDetailViewPage(@PathVariable Long visitId, Model model) {
        Optional<VisitEntity> visitOptional = visitService.getVisitId(visitId);

        if (visitOptional.isPresent()) {
            VisitEntity visit = visitOptional.get();
            VisitDetailViewDTO visitDetailViewDTO = (VisitDetailViewDTO) dtoConverter.convertToDTO(visit,
                    new VisitDetailViewDTO());

            model.addAttribute("visitDetail", visitDetailViewDTO);
        } else {
            return "not-found";
        }

        return "visit";
    }

    @GetMapping(path = "/visit/{visitId}/edit")
    public String showEditVisitForm(@PathVariable Long visitId, Model model) {
        Optional<VisitEntity> visitOptional = visitService.getVisitId(visitId);

        if (visitOptional.isPresent()) {
            VisitEntity visit = visitOptional.get();
            VisitCreateDTO visitCreateDTO = (VisitCreateDTO) dtoConverter.convertToDTO(visit, new VisitCreateDTO());

            model.addAttribute("visitCreateDTO", visitCreateDTO);
            return "edit";
        } else {
            return "not-found";
        }
    }

    @PostMapping(path = "/visit/{visitId}/edit")
    public String postEditVisitForm(@PathVariable Long visitId, @Valid VisitCreateDTO visitCreateDTO,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        List<VisitEntity> visitEntities = visitService.getVisitsByDentistIdAndDateAndTimeWithoutSameVisitId(
                visitCreateDTO.getDentistId(), visitCreateDTO.getDate(), visitCreateDTO.getTime(),
                visitCreateDTO.getVisitId());

        if (!visitEntities.isEmpty()) {
            bindingResult.addError(new FieldError("visitCreateDTO", "dentistId",
                    "registration.visitExists.error"));
            return "edit";
        }

        VisitEntity visitEntity = (VisitEntity) dtoConverter.convertToEntity(visitCreateDTO, new VisitEntity());
        visitService.updateVisit(visitId, visitEntity);

        Long clientId = visitCreateDTO.getClientId();
        ClientEntity clientEntity = (ClientEntity) dtoConverter.convertToEntity(visitCreateDTO, new ClientEntity());
        clientService.updateClient(clientId, clientEntity);

        redirectAttributes.addFlashAttribute("updatedSuccessMessage", "visit.updated.successMessage");

        return "redirect:/visit/" + visitCreateDTO.getVisitId();
    }

    @PostMapping("/visit/{visitId}/delete")
    public String deleteVisit(@PathVariable Long visitId) {
        visitService.deleteVisit(visitId);

        return "redirect:/visits";
    }

    @ModelAttribute(name = "visitOptionsDTO")
    private VisitOptionsDTO getVisitOptionsDTO() {
        List<DentistDTO> dentistList = dentistService.getAllDentists().stream()
                .map(dentist -> (DentistDTO) dtoConverter.convertToDTO(dentist, new DentistDTO()))
                .collect(Collectors.toList());

        List<ProcedureDTO> procedureList = procedureService.getAllProcedures().stream()
                .map(procedure -> (ProcedureDTO) dtoConverter.convertToDTO(procedure, new ProcedureDTO()))
                .collect(Collectors.toList());

        List<String> timeList = Arrays.asList("9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00");

        VisitOptionsDTO visitOptionsDTO = new VisitOptionsDTO();
        visitOptionsDTO.setDentistList(dentistList);
        visitOptionsDTO.setProcedureList(procedureList);
        visitOptionsDTO.setTimeList(timeList);

        return visitOptionsDTO;
    }
}
