package com.docapp.api.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.docapp.api.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM Appointment WHERE ID = ?1")
	int deleteByAppointmentId(Long id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Appointment WHERE patientId = ?1")
	int deleteByAppointmentbyPatientId(Long id);
	
	Optional<Appointment> findByDateOfAppointment(Date dateOfAppointment);
	
	@Query("SELECT a FROM Appointment a WHERE a.patientId = ?1 AND a.dateOfAppointment <= ?2")
	List<Appointment> findAppointmentsBeforeToday(Long id, Date now);
	
	@Query("SELECT a FROM Appointment a WHERE a.patientId = ?1 AND a.dateOfAppointment >= ?2")
	List<Appointment> findAppointmentsAfterToday(Long id, Date now);
	
	@Query("SELECT a FROM Appointment a WHERE a.dateOfAppointment >= ?1 AND a.dateOfAppointment <= ?2")
	List<Appointment> findAppointmentsBetween(Date dateFrom, Date dateTo);
}
