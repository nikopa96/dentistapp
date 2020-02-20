package com.cgi.dentistapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "visit", schema = "dentistapp")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VisitEntity extends AbstractEntity {

    @Id
    @Column(name = "visit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Temporal(TemporalType.TIME)
    @Column(name = "time")
    private Date time;

    @OneToOne
    @JoinColumn(name = "dentist_id")
    private DentistEntity dentist;

    @OneToOne
    @JoinColumn(name = "procedure_id")
    private ProcedureEntity procedure;

    @OneToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;
}
