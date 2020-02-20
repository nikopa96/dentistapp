package com.cgi.dentistapp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "dentist", schema = "dentistapp")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DentistEntity extends AbstractEntity {

    @Id
    @Column(name = "dentist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dentistId;

    @Column(name = "name")
    private String name;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;
}
