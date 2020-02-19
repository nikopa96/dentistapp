DROP SCHEMA IF EXISTS dentistapp CASCADE;
CREATE SCHEMA IF NOT EXISTS dentistapp;

CREATE TABLE dentistapp.dentist (
  dentist_id INT AUTO_INCREMENT,
  name VARCHAR (255) NOT NULL,
  telephone VARCHAR (50) NOT NULL,
  email VARCHAR (255) NOT NULL,
  CONSTRAINT pk_dentist_id PRIMARY KEY (dentist_id)
);

CREATE TABLE dentistapp.procedure (
  procedure_id INT AUTO_INCREMENT,
  name VARCHAR (255) NOT NULL,
  price DECIMAL (10, 2) NOT NULL,
  CONSTRAINT pk_procedure_id PRIMARY KEY (procedure_id)
);

CREATE TABLE dentistapp.client (
  client_id BIGINT AUTO_INCREMENT,
  first_name VARCHAR (255) NOT NULL,
  last_name VARCHAR (255) NOT NULL,
  telephone VARCHAR (50) NOT NULL,
  email VARCHAR (255) NOT NULL,
  connection_type VARCHAR (5) NOT NULL,
  CONSTRAINT pk_client_id PRIMARY KEY (client_id)
);

CREATE TABLE dentistapp.visit (
  visit_id BIGINT AUTO_INCREMENT,
  dentist_id INT NOT NULL,
  procedure_id INT NOT NULL,
  date DATE NOT NULL,
  time TIME NOT NULL,
  client_id BIGINT NOT NULL,
  CONSTRAINT pk_visit_id PRIMARY KEY (visit_id),
  CONSTRAINT fk_visit_dentist_id FOREIGN KEY (dentist_id) REFERENCES dentistapp.dentist(dentist_id)
    ON UPDATE CASCADE ON DELETE NO ACTION,
  CONSTRAINT fk_visit_procedure_id FOREIGN KEY (procedure_id) REFERENCES dentistapp.procedure(procedure_id)
    ON UPDATE CASCADE ON DELETE NO ACTION,
  CONSTRAINT fk_visit_client_id FOREIGN KEY (client_id) REFERENCES dentistapp.client(client_id)
    ON UPDATE CASCADE ON DELETE NO ACTION
);
CREATE INDEX idx_visit_dentist_id ON dentistapp.visit(dentist_id);
CREATE INDEX idx_visit_procedure_id ON dentistapp.visit(procedure_id);
CREATE INDEX idx_visit_client_id ON dentistapp.visit(client_id);
