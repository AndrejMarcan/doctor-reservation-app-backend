package com.docapp.api.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.docapp.api.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM Account WHERE PATIENT_ID = ?1")
	int deleteByPatientId(Long id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Account WHERE ID = ?1")
	int deleteByAccountId(Long id);
	
	Optional<Account> findByPatientId(Long id);
	
	Optional<Account> findByUsername(String username);
	
	List<Account> findByRole(String role);
}
