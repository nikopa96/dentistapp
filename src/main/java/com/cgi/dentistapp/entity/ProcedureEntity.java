package com.cgi.dentistapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "procedure", schema = "dentistapp")
@Getter
@Setter
public class ProcedureEntity extends AbstractEntity {

    @Id
    @Column(name = "procedure_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer procedureId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;
}
