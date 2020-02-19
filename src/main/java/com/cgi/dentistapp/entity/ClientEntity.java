package com.cgi.dentistapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "client", schema = "dentistapp")
@Getter
@Setter
public class ClientEntity extends AbstractEntity {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "connection_type")
    private String connectionType;
}
