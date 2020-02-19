package com.cgi.dentistapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VisitOptionsDTO extends AbstractDTO {

    private List<DentistDTO> dentistList;
    private List<ProcedureDTO> procedureList;
    private List<String> timeList;
}
