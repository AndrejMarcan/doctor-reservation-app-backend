package com.docapp.api.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.docapp.api.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByBirthNumber(Long birthNumber);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Patient WHERE ID = ?1")
	int deleteByPatientId(Long id);
	
	@Query("SELECT p FROM Patient p WHERE p.firstname = ?1 AND p.surename = ?2")
	List<Patient> findPatientsByFirstnameAndSurename(String firstName, String surename);
	
	@Query("SELECT p FROM Patient p WHERE p.firstname = ?1")
	List<Patient> findPatientsByFirstname(String firstName);
	
	@Query("SELECT p FROM Patient p WHERE p.surename = ?1")
	List<Patient> findPatientsBySurename(String surename);
	
	@Query("SELECT p FROM Patient p WHERE p.birthNumber = ?1")
	List<Patient> findPatientsByBirthNumber(Long birthNumber);
	
	List<Patient> findByIdIn(List<Long> id);
	
}
