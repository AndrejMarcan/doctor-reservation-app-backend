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

import com.docapp.api.model.Appointment;
import com.docapp.api.model.AppointmentDateRequest;
import com.docapp.api.service.AppointmentService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class AppointmentController {

	private final AppointmentService appointmentService;
	
	public AppointmentController(@Autowired AppointmentService appointmentService) {
		super();
		this.appointmentService = appointmentService;
	}
	
	@PostMapping(value = "/appointment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Appointment> createAppointment(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@RequestBody Appointment request) {
		Appointment response = appointmentService.createNewAppointment(request);
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/appointment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Appointment> getAppointmentDetail(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id){
		Appointment response = appointmentService.getAppointmentDetails(id);
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/appointments/past/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Appointment>> getPastAppointments(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("patientId") Long id) {
		List<Appointment> response = appointmentService.getPastAppointmentsForPatient(id);
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/appointments/future/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Appointment>> getFutureAppointments(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("patientId") Long id) {
		List<Appointment> response = appointmentService.getFutureAppointmentsForPatient(id);
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/appointments/today", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Appointment>> getTodayAppointments(@RequestHeader(value = "Authorization", required = true) String authorization) {
		List<Appointment> response = appointmentService.getAppointmentsForToday();
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/appointments/between", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Appointment>> getAppointmentsBetween(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@RequestBody AppointmentDateRequest request) {
		List<Appointment> response = appointmentService.getAppointmentsBetween(request);
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}
	
	@PatchMapping(value = "/appointment/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Appointment> updateAppointment(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id, @RequestBody Appointment request){
		Appointment response = appointmentService.updateAppointment(id, request);
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/appointment/{id}")
	public ResponseEntity<Void> deleteAppointmentById(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id) {
		appointmentService.deleteAppointmentById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
