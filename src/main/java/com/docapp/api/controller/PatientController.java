package com.docapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docapp.api.model.Patient;
import com.docapp.api.model.PatientSearchRequest;
import com.docapp.api.service.PatientService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class PatientController {
	private final PatientService patientService;
	

	public PatientController(@Autowired PatientService patientService) {
		super();
		this.patientService = patientService;
	}

	@PostMapping(value = "/patient", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Patient> createPatient(@RequestHeader(value = "Authorization", required = true) String authorization, @RequestBody Patient request) {
		Patient response = patientService.createPatient(request);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Patient> getPatient(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id) {
		Patient response = patientService.getPatientDetail(id);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Patient>> getPatients(@RequestHeader(value = "Authorization", required = true) String authorization) {
		List<Patient> response = patientService.getPatients();	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/patients/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Patient>> getPatientsByRequest(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@RequestBody PatientSearchRequest request) {
		List<Patient> response = patientService.getPatientsByRequest(request);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/patient/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Patient> updatePatient(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id, @RequestBody Patient request) {
		Patient response = patientService.updatePatient(id, request);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/patient/{id}")
	public ResponseEntity<Void> deletePatient(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id) {
		patientService.deletePatient(id);	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/patient/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Patient> getPatientByUsername(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("username") String username) {
		Patient response = patientService.getByUsername(username);	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
