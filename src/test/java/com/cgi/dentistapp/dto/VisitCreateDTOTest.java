package com.cgi.dentistapp.dto;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class VisitCreateDTOTest {

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static Validator validator;

    @Before
    public void setUp() {
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.timeFormat = new SimpleDateFormat("HH:mm");
    }

    @BeforeClass
    public static void createValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void shouldNotAcceptNullOrEmptyStrings() throws ParseException {
        VisitCreateDTO visitCreate1 = VisitCreateDTO.builder()
                .date(dateFormat.parse("20.02.2020"))
                .time(timeFormat.parse("12:00"))
                .dentistId(1)
                .procedureId(1)
                .clientFirstName("Clayton")
                .clientLastName("Norris")
                .clientTelephone("+372 57986417")
                .clientEmail("norris@yahoo.com")
                .clientConnectionType("PHONE")
                .build();

        VisitCreateDTO visitCreate2 = VisitCreateDTO.builder()
                .clientFirstName("")
                .clientLastName("")
                .clientTelephone("")
                .clientEmail("")
                .build();

        VisitCreateDTO visitCreate3 = VisitCreateDTO.builder()
                .build();

        Set<ConstraintViolation<VisitCreateDTO>> violations1 = validator.validate(visitCreate1);
        Set<ConstraintViolation<VisitCreateDTO>> violations2 = validator.validate(visitCreate2);
        Set<ConstraintViolation<VisitCreateDTO>> violations3 = validator.validate(visitCreate3);

        assertEquals(0, violations1.size());
        assertEquals(10, violations2.size());
        assertEquals(9, violations3.size());
    }

    @Test
    public void shouldNotAcceptWrongPatterns() throws ParseException {
        VisitCreateDTO visitCreate = VisitCreateDTO.builder()
                .date(dateFormat.parse("20.02.2020"))
                .time(timeFormat.parse("12:00"))
                .dentistId(1)
                .procedureId(1)
                .clientFirstName("Clayton")
                .clientLastName("Norris")
                .clientTelephone("579-abc-86417")
                .clientEmail("norris?yahoo.com")
                .clientConnectionType("someText")
                .build();

        Set<ConstraintViolation<VisitCreateDTO>> violations = validator.validate(visitCreate);

        assertEquals(3, violations.size());
    }
}
