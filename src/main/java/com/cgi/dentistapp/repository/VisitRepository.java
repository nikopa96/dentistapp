package com.cgi.dentistapp.repository;

import com.cgi.dentistapp.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity, Long> {

    @Query("select ve from VisitEntity ve where ve.dentist.dentistId = :dentistId and " +
            "ve.date = :date and ve.time = :time")
    List<VisitEntity> findAllByDentistIdAndDateAndTime(@Param("dentistId") Integer dentistId, @Param("date") Date date,
                                                       @Param("time") Date time);

    @Query("select ve from VisitEntity ve where ve.dentist.dentistId = :dentistId and " +
            "ve.date = :date and ve.time = :time and ve.visitId <> :visitId")
    List<VisitEntity> findAllByDentistIdAndDateAndTimeWithoutSameVisitId(@Param("dentistId") Integer dentistId,
                                                                         @Param("date") Date date,
                                                                         @Param("time") Date time,
                                                                         @Param("visitId") Long visitId);

    @Query("select ve from VisitEntity ve where lower(ve.dentist.name) like %:request% or " +
            "lower(ve.client.firstName) like %:request% or lower(ve.client.lastName) like %:request%")
    List<VisitEntity> findAllBySearchRequest(@Param("request") String request);
}
