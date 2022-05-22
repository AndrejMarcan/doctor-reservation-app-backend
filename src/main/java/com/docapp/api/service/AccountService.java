package com.docapp.api.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docapp.api.model.Account;
import com.docapp.api.model.Patient;
import com.docapp.api.repository.AccountRepository;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
	
	public AccountService(@Autowired AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}
	
	/** Create account - admin role */
	public Account createAccount(Account newAccountRequest) {
		newAccountRequest.setPatient(null);
		return accountRepository.save(newAccountRequest);
	}
	
	/** Create account for patient during registration - doctor role*/
	public Account createAccountFromPatient(Patient patient) {
		Account account = new Account();
		account.setUsername(patient.getFirstname() + "." + patient.getSurename());
		account.setPassword(generateDefaultPassword());
		account.setPatient(patient);
		account.setRole("PATIENT_ROLE");
		return accountRepository.save(account);
	}
	
	private String generateDefaultPassword() {
		return "test12345";
	}
	
	/** Delete account by patient ID */
	public void deleteByPatientId(Long id) {
		accountRepository.deleteByPatientId(id);
	}
	
	/** Delete account by account ID */
	public void deleteById(Long id) {
		accountRepository.deleteByAccountId(id);
	}
	
	/** Fetch account details */
	public Account getAccountById(Long id) {
		
		Optional<Account> account = accountRepository.findById(id);
		if(account.isEmpty()) {
			return null;
		}
		
		return account.get();
	}

	/** Update password */
	public Account updateAccountById(Long id, Account request) {
		Optional<Account> account = accountRepository.findById(id);
		
		if(account.isEmpty()) {
			return null;
		}
		
		Account accountToUpdate = account.get();
		
		if(!StringUtils.isBlank(request.getPassword())) {
			accountToUpdate.setPassword(request.getPassword());
		}
		
		if(!StringUtils.isBlank(request.getUsername())) {
			accountToUpdate.setUsername(request.getUsername());
		}
		
		return accountRepository.save(accountToUpdate);
	}
	
	public Account getAccountByUsername(String username) {
		return accountRepository.findByUsername(username).get();
	}

	public List<Account> getDoctors() {
		return accountRepository.findByRole("DOCTOR_ROLE");
	}
}
