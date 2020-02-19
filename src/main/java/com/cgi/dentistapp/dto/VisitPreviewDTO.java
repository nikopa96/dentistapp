package com.cgi.dentistapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VisitPreviewDTO extends AbstractDTO {

    private Long visitId;
    private Date date;
    private Date time;
    private String dentistName;
    private String procedureName;
}
