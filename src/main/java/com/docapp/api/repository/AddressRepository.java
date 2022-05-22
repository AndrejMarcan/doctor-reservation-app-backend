package com.docapp.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.docapp.api.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM Address WHERE ID = ?1")
	int deleteByAddressId(Long id);
}
