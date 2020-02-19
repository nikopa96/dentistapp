package com.cgi.dentistapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class VisitDetailViewDTO extends AbstractDTO {

    private Long visitId;
    private Date date;
    private Date time;
    private Integer dentistId;
    private String dentistName;
    private String dentistTelephone;
    private String dentistEmail;
    private Integer procedureId;
    private String procedureName;
    private BigDecimal procedurePrice;
    private Long clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientTelephone;
    private String clientEmail;
    private String clientConnectionType;
}
