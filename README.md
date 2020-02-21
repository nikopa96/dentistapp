# DentistApp
* **Time spent: total 5 days**: 4 days to write a program (including research and 
creation of Thymeleaf templates) + 1 day to write unit tests
* **What was easy?**: Creating Thymeleaf templates using Bootstrap. Сreating Service and Repository classes, 
writing HQL queries (Hibernate Query Language).
* **What was difficult?**: DTO Pattern. I used to work directly with Entity and did not use DTO.
These articles helped me understand why this pattern is needed:
    * [Why, When and How to Use DTO Projections with JPA and Hibernate](https://thoughts-on-java.org/dto-projections/)
    * [Automatically Mapping DTO to Entity on Spring Boot APIs](https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/)
    * [Full Code Example](https://github.com/hackmajoris/java-dto-mapping/tree/master/src/main/java/com/hackmajoris/entitytodto/model)

## Database schema
![Database schema](/schema.png)

DentistApp database consists of 4 tables: Dentist, Procedure, Visit, Client.
The most important table is Visit: this table has a One-to-One relationship with all other
tables in the database.

We divided the Visit entity into 4 tables, because in the future other entities (Dentist,
Procedure, Client) can be used independently of the visit. 
For each table its own Java Entity class was created.

## DTO classes
We do not want to directly use Entity classes in Thymeleaf Views. We want to hide some of the
fields in the classes and vice versa remove dependecy injection to conveniently work with
data. To solve these problems we convert Entity to DTO classes using ModelMapper and 
DTOConverter class.

1. **AbstractDTO**: Abstract class. All DTO classes in this project inherit from this
abstract class. This is necessary in order to reduce the amount of code in the DTOConverter 
class.
2. **DentistDTO**: Dentist entity where some fields are hidden. Only for reading.
3. **ProcedureDTO**: Procedure entity where some fields are hidden. Only for reading.
4. **VisitCreateDTO**: Only for writing. We use this class to add a new visit to the
database or to update an existing visit.
5. **VisitDetailViewDTO**: Designed to represent absolutely all information about the visit
(with dentist, procedure and client fields). Only for reading. Used on the Detail View page.
6. **VisitPreviewDTO**: Designed to display only part of the information about the visit.
Only for reading. Used on the All Visits page and Search page.
7. **VisitOptionsDTO**: Dentist, procedures and times options in ArrayLists. Needed to
select a dentist, procedure and time on the page for adding or updating a visit.
Only for reading.

Field validation is only implemented for the **VisitCreateDTO** class, because only
in this class we write data. All other classes are used only for reading and it makes no sense
to validate them.

## Requests
1. ``GET /`` Show the form for adding a visit
2. ``POST /`` Send form data to the server and add a visit to the database
3. ``GET /visits`` All added visits (as VisitPreviewDTOs)
4. ``GET /visits/search`` Get visits by search request
5. ``GET /visit/{visitId}`` Visit detail view (as VisitDetailViewDTO)
6. ``GET /visit/{visitId}/edit`` Show Edit visit form
7. ``POST /visit/{visitId}/edit`` Send updated visit to server and update in database
8. ``POST /visit/{visitId}/delete`` Delete visit

## Unit tests
Unit tests are in the test folder