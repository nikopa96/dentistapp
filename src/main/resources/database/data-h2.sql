INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Linda Torres', '+372 54201962', 'torres@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Henry Ross', '+372 57864108', 'henry.r@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Susan Ramirez', '+372 55566901', 'susram@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Walter Hernandez', '+372 59012871', 'walter@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Amy Thomas', '+372 57643108', 'amy.thomas@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('James Butler', '+372 56901254', 'james.butler@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Doris Roberts', '+372 57098134', 'doris.roberts@gmail.com');
INSERT INTO dentistapp.dentist(name, telephone, email) VALUES ('Julie Sanders', '+372 59012356', 'julie@protonmail.com');

INSERT INTO dentistapp.procedure(name, price) VALUES ('Konsultatsioon', 10);
INSERT INTO dentistapp.procedure(name, price) VALUES ('Hambaravi', 40);
INSERT INTO dentistapp.procedure(name, price) VALUES ('Hamba eemaldamine', 80);
INSERT INTO dentistapp.procedure(name, price) VALUES ('Proteseerimine', 200);
INSERT INTO dentistapp.procedure(name, price) VALUES ('Implantoloogia', 350);

INSERT INTO dentistapp.client(first_name, last_name, telephone, email, connection_type)
  VALUES ('Anne', 'Sildam', '+372 59005148', 'anne@gmail.com', 'PHONE');

INSERT INTO dentistapp.visit(dentist_id, procedure_id, date, time, client_id)
  VALUES (4, 2, '2020-02-14', '15:00', 1);
