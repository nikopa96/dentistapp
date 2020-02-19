package com.cgi.dentistapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcedureDTO extends AbstractDTO {

    private Integer procedureId;
    private String name;
}
