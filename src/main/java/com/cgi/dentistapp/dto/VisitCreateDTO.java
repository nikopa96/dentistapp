package com.cgi.dentistapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VisitCreateDTO extends AbstractDTO {

    private Integer visitId;

    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date date;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private Date time;

    @NotNull
    private Integer dentistId;

    @NotNull
    private Integer procedureId;

    private Long clientId;

    @NotNull
    @Size(min = 1, max = 255)
    private String clientFirstName;

    @NotNull
    @Size(min = 1, max = 255)
    private String clientLastName;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[+]*[(]?[+]?[0-9]*[)]?[-\\s\\d]*$")
    private String clientTelephone;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^([a-z0-9_\\-.]+)@([a-z0-9_\\-.]+)\\.([a-z]{2,5})$")
    private String clientEmail;

    @NotNull
    @Pattern(regexp = "PHONE|EMAIL")
    private String clientConnectionType;
}
