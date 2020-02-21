package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dto.VisitCreateDTO;
import com.cgi.dentistapp.entity.ClientEntity;
import com.cgi.dentistapp.entity.DentistEntity;
import com.cgi.dentistapp.entity.ProcedureEntity;
import com.cgi.dentistapp.entity.VisitEntity;
import com.cgi.dentistapp.repository.ClientRepository;
import com.cgi.dentistapp.repository.VisitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DentistAppControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private VisitRepository visitRepository;

    @MockBean
    private ClientRepository clientRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldShowRegisterForm() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(view().name("form"));
    }

    @Test
    public void shouldReturnFormIfVisitCreateDTOIsIncorrect() throws Exception {
        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr("visitCreateDTO", new VisitCreateDTO())
        ).andExpect(view().name("form"));
    }

    @Test
    public void shouldReturnFormIfVisitDentistIdAndDateAndTimeAlreadyExists() throws Exception {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse("20.02.2020");
        Date time = new SimpleDateFormat("HH:mm").parse("15:00");

        VisitEntity existingVisit = VisitEntity.builder()
                .date(date)
                .time(time)
                .build();

        when(visitRepository.findAllByDentistIdAndDateAndTime(1, date, time))
                .thenReturn(Collections.singletonList(existingVisit));

        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("date", "20.02.2020")
                .param("time", "15:00")
                .param("dentistId", "1")
                .param("procedureId", "1")
                .param("clientFirstName", "Clayton")
                .param("clientLastName", "Norris")
                .param("clientTelephone", "+372 57986417")
                .param("clientEmail", "norris@yahoo.com")
                .param("clientConnectionType", "PHONE")
                .sessionAttr("visitCreateDTO", new VisitCreateDTO())
        ).andExpect(view().name("form"));

        verify(visitRepository, times(1)).findAllByDentistIdAndDateAndTime(1, date, time);
        verifyNoMoreInteractions(visitRepository);
    }

    @Test
    public void shouldReturnVisitPageIfVisitCreateDTOIsCorrect() throws Exception {
        ClientEntity addedClient = ClientEntity.builder()
                .clientId(5L)
                .build();
        VisitEntity addedVisit = VisitEntity.builder()
                .visitId(5L)
                .build();

        when(visitRepository.findAllByDentistIdAndDateAndTime(anyInt(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(clientRepository.save(any())).thenReturn(addedClient);
        when(visitRepository.save(any())).thenReturn(addedVisit);

        this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("date", "20.02.2020")
                .param("time", "15:00")
                .param("dentistId", "1")
                .param("procedureId", "1")
                .param("clientFirstName", "Clayton")
                .param("clientLastName", "Norris")
                .param("clientTelephone", "+372 57986417")
                .param("clientEmail", "norris@yahoo.com")
                .param("clientConnectionType", "PHONE")
                .sessionAttr("visitCreateDTO", new VisitCreateDTO())
        ).andExpect(view().name("redirect:/visit/5"));

        verify(visitRepository, times(1)).findAllByDentistIdAndDateAndTime(anyInt(), any(), any());
        verify(clientRepository, times(1)).save(any());
        verify(visitRepository, times(1)).save(any());
        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(visitRepository);
    }

    @Test
    public void shouldShowVisitsPreviews() throws Exception {
        Date date1 = new SimpleDateFormat("dd.MM.yyyy").parse("20.02.2020");
        Date time1 = new SimpleDateFormat("HH:mm").parse("18:00");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy").parse("22.02.2020");
        Date time2 = new SimpleDateFormat("HH:mm").parse("13:00");

        VisitEntity visit1 = VisitEntity.builder()
                .visitId(1L)
                .date(date1)
                .time(time1)
                .dentist(DentistEntity.builder().name("Mariana Friedman").build())
                .procedure(ProcedureEntity.builder().name("Hambaravi").build())
                .build();

        VisitEntity visit2 = VisitEntity.builder()
                .visitId(2L)
                .date(date2)
                .time(time2)
                .dentist(DentistEntity.builder().name("Elisa Roberson").build())
                .procedure(ProcedureEntity.builder().name("Konsultatsioon").build())
                .build();

        when(visitRepository.findAll()).thenReturn(Arrays.asList(visit1, visit2));

        this.mockMvc.perform(get("/visits"))
                .andExpect(view().name("visits"))
                .andExpect(model().attribute("visitsPreviewDTOs", hasSize(2)));

        verify(visitRepository, times(1)).findAll();
        verifyNoMoreInteractions(visitRepository);
    }

    @Test
    public void shouldVisitFoundVisitPreviewsBySearchRequest() throws Exception {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse("20.02.2020");
        Date time = new SimpleDateFormat("HH:mm").parse("18:00");

        VisitEntity visit = VisitEntity.builder()
                .visitId(2L)
                .date(date)
                .time(time)
                .dentist(DentistEntity.builder().name("Elisa Roberson").build())
                .procedure(ProcedureEntity.builder().name("Konsultatsioon").build())
                .build();

        String searchRequest = "roberson";
        when(visitRepository.findAllBySearchRequest(searchRequest)).thenReturn(Collections.singletonList(visit));

        this.mockMvc.perform(get("/visits/search?request=" + searchRequest))
                .andExpect(view().name("visits"))
                .andExpect(model().attribute("visitsPreviewDTOs", hasSize(1)));

        verify(visitRepository, times(1)).findAllBySearchRequest(searchRequest);
        verifyNoMoreInteractions(visitRepository);
    }

    @Test
    public void shouldShowVisitDetailViewPage() throws Exception {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse("20.02.2020");
        Date time = new SimpleDateFormat("HH:mm").parse("18:00");

        DentistEntity dentist = DentistEntity.builder().dentistId(1).name("Elisa Roberson").telephone("+372 58454391")
                .email("roberson@gmail.com").build();

        ProcedureEntity procedure = ProcedureEntity.builder().procedureId(1).name("Konsultatsioon")
                .price(new BigDecimal("15.00")).build();

        ClientEntity client = ClientEntity.builder().clientId(1L).firstName("Luella").lastName("Golden")
                .telephone("+372 56875480").email("luella@gmail.com").connectionType("PHONE").build();

        VisitEntity visit = VisitEntity.builder().visitId(2L).date(date).time(time).dentist(dentist)
                .procedure(procedure).client(client).build();

        when(visitRepository.findById(visit.getVisitId())).thenReturn(Optional.of(visit));

        this.mockMvc.perform(get("/visit/{visitId}", visit.getVisitId()))
                .andExpect(view().name("visit"))
                .andExpect(model().attribute("visitDetail", hasProperty("visitId", is(2L))))
                .andExpect(model().attribute("visitDetail", hasProperty("date", is(date))))
                .andExpect(model().attribute("visitDetail", hasProperty("time", is(time))))
                .andExpect(model().attribute("visitDetail", hasProperty("dentistId", is(1))))
                .andExpect(model().attribute("visitDetail", hasProperty("dentistName",
                        is("Elisa Roberson"))))
                .andExpect(model().attribute("visitDetail", hasProperty("dentistTelephone",
                        is("+372 58454391"))))
                .andExpect(model().attribute("visitDetail", hasProperty("dentistEmail",
                        is("roberson@gmail.com"))))
                .andExpect(model().attribute("visitDetail", hasProperty("procedureId",
                        is(1))))
                .andExpect(model().attribute("visitDetail", hasProperty("procedureName",
                        is("Konsultatsioon"))))
                .andExpect(model().attribute("visitDetail", hasProperty("procedurePrice",
                        is(new BigDecimal("15.00")))))
                .andExpect(model().attribute("visitDetail", hasProperty("clientId", is(1L))))
                .andExpect(model().attribute("visitDetail", hasProperty("clientFirstName",
                        is("Luella"))))
                .andExpect(model().attribute("visitDetail", hasProperty("clientLastName",
                        is("Golden"))))
                .andExpect(model().attribute("visitDetail", hasProperty("clientTelephone",
                        is("+372 56875480"))))
                .andExpect(model().attribute("visitDetail", hasProperty("clientEmail",
                        is("luella@gmail.com"))))
                .andExpect(model().attribute("visitDetail", hasProperty("clientConnectionType",
                        is("PHONE"))));

        verify(visitRepository, times(1)).findById(visit.getVisitId());
        verifyNoMoreInteractions(visitRepository);
    }

    @Test
    public void shouldShowEditVisitForm() throws Exception {
        VisitEntity visit = VisitEntity.builder()
                .visitId(1L)
                .build();

        when(visitRepository.findById(visit.getVisitId())).thenReturn(Optional.of(visit));

        this.mockMvc.perform(get("/visit/{visitId}/edit", visit.getVisitId()))
                .andExpect(view().name("edit"));

        verify(visitRepository, times(1)).findById(visit.getVisitId());
        verifyNoMoreInteractions(visitRepository);
    }

    @Test
    public void shouldPostEditVisitForm() throws Exception {
        VisitEntity oldVisit = VisitEntity.builder()
                .visitId(2L)
                .build();

        ClientEntity oldClient = ClientEntity.builder()
                .clientId(2L)
                .build();

        when(visitRepository.findAllByDentistIdAndDateAndTimeWithoutSameVisitId(anyInt(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(visitRepository.findById(oldVisit.getVisitId())).thenReturn(Optional.of(oldVisit));
        when(clientRepository.findById(oldClient.getClientId())).thenReturn(Optional.of(oldClient));

        this.mockMvc.perform(post("/visit/{visitId}/edit", oldVisit.getVisitId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("visitId", "2")
                .param("date", "20.02.2020")
                .param("time", "15:00")
                .param("dentistId", "1")
                .param("procedureId", "1")
                .param("clientId", "2")
                .param("clientFirstName", "Clayton")
                .param("clientLastName", "Norris")
                .param("clientTelephone", "+372 57986417")
                .param("clientEmail", "norris@yahoo.com")
                .param("clientConnectionType", "PHONE")
                .sessionAttr("visitCreateDTO", new VisitCreateDTO())
        ).andExpect(view().name("redirect:/visit/2"));

        verify(visitRepository, times(1)).findAllByDentistIdAndDateAndTimeWithoutSameVisitId(
                anyInt(), any(), any(), anyLong());
        verify(visitRepository, times(1)).findById(oldVisit.getVisitId());
        verify(clientRepository, times(1)).findById(oldClient.getClientId());
        verify(visitRepository, times(1)).save(any());
        verify(clientRepository, times(1)).save(any());
        verifyNoMoreInteractions(visitRepository);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void shouldRedirectAfterVisitDelete() throws Exception {
        VisitEntity visit = VisitEntity.builder()
                .visitId(1L)
                .build();

        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));

        this.mockMvc.perform(post("/visit/{visitId}/delete", 1L))
                .andExpect(view().name("redirect:/visits"));

        verify(visitRepository, times(1)).findById(1L);
        verify(visitRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(visitRepository);
    }
}
