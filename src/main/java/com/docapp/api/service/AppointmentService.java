package com.docapp.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docapp.api.model.Appointment;
import com.docapp.api.model.AppointmentDateRequest;
import com.docapp.api.repository.AppointmentRepository;

@Service
public class AppointmentService {
	private final AppointmentRepository appointmentRepo;
	
	public AppointmentService(@Autowired AppointmentRepository appointmentRepo) {
		super();
		this.appointmentRepo = appointmentRepo;
	}
	
	/** Create new Appointment */
	@SuppressWarnings("deprecation")
	public Appointment createNewAppointment(Appointment request) {
		/** Ordination hours */
		if(request.getDateOfAppointment().getHours() < 8 || request.getDateOfAppointment().getHours() > 17) {
			return null;
		}
		
		/** Check existing appointment */
		Optional<Appointment> appointment = appointmentRepo.findByDateOfAppointment(request.getDateOfAppointment());
		/** If appointment exists on that hour return null */
		if(appointment.isPresent()) {
			return null;
		}
		
		/** Set date of creation */
		request.setDateOfCreation(new Date(System.currentTimeMillis()));
		/** Save data to DB */
		return appointmentRepo.save(request);
	}

	/** Get Appointment details */
	public Appointment getAppointmentDetails(Long id) {
		/** Get Appointment */
		Optional<Appointment> appointment = appointmentRepo.findById(id);
		/** If emplty return null */
		if(appointment.isEmpty()) {
			return null;
		}
		
		Appointment response = appointment.get();
		response.setDate(getDateFromDBDate(response.getDateOfAppointment()));
		response.setTime(getTimeFromDBDate(response.getDateOfAppointment()));
		/** Return details */
		return response;
	}
	
	/** Get list of past Appointments for patient by its ID */
	public List<Appointment> getPastAppointmentsForPatient(Long patientId){
		List<Appointment> appointmentsDb = appointmentRepo.findAppointmentsBeforeToday(patientId, new Date(System.currentTimeMillis()));
		return appointmentsDb.stream().map(
				a -> {
					a.setDate(getDateFromDBDate(a.getDateOfAppointment()));
					a.setTime(getTimeFromDBDate(a.getDateOfAppointment()));
					return a;
					}
				).collect(Collectors.toList());
	}
	
	/** Get list of future Appointments for patient by its ID */
	public List<Appointment> getFutureAppointmentsForPatient(Long patientId){
		List<Appointment> appointmentsDb = appointmentRepo.findAppointmentsAfterToday(patientId, new Date(System.currentTimeMillis()));
		return appointmentsDb.stream().map(
				a -> {
					a.setDate(getDateFromDBDate(a.getDateOfAppointment()));
					a.setTime(getTimeFromDBDate(a.getDateOfAppointment()));
					return a;
					}
				).collect(Collectors.toList());
	}
	
	/** Get list of Appointments for today - doctor */
	public List<Appointment> getAppointmentsForToday() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 2);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date dateFrom = new Date(calendar.getTimeInMillis());
				
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);		
		Date dateTo = new Date(calendar.getTimeInMillis());
		
		List<Appointment> appointmentsDb = appointmentRepo.findAppointmentsBetween(dateFrom, dateTo);
		
		return appointmentsDb.stream().map(
				a -> {
					a.setDate(getDateFromDBDate(a.getDateOfAppointment()));
					a.setTime(getTimeFromDBDate(a.getDateOfAppointment()));
					return a;
					}
				).collect(Collectors.toList());
	}
	
	/** Get list of Appointments between given dates - doctor */
	public List<Appointment> getAppointmentsBetween(AppointmentDateRequest request) {
		List<Appointment> appointmentsDb = appointmentRepo.findAppointmentsBetween(request.getDateFrom(), request.getDateTo());
		return appointmentsDb.stream().map(
				a -> {
					a.setDate(getDateFromDBDate(a.getDateOfAppointment()));
					a.setTime(getTimeFromDBDate(a.getDateOfAppointment()));
					return a;
					}
				).collect(Collectors.toList());
	}

	/** Update date of appointment or reason */
	@SuppressWarnings("deprecation")
	public Appointment updateAppointment(Long id, Appointment request) {
		Optional<Appointment> appointment = appointmentRepo.findById(id);
		if(appointment.isEmpty()) {
			return null;
		}
		Appointment appointmentToUpdate = appointment.get();
		
		if(request.getDateOfAppointment() != null) {
			if(request.getDateOfAppointment().getHours() > 7 || request.getDateOfAppointment().getHours() < 15) {
				appointmentToUpdate.setDateOfAppointment(request.getDateOfAppointment());
			}
		}
		
		if(!StringUtils.isBlank(request.getReason())) {
			appointmentToUpdate.setReason(request.getReason());
		}
		
		return appointmentRepo.save(appointmentToUpdate);
	}
	
	/** Delete Appointment*/
	public void deleteAppointmentById(Long id) {
		appointmentRepo.deleteByAppointmentId(id);
	}
	
	private String getDateFromDBDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");  
		return dateFormat.format(date);
	}
	
	private String getTimeFromDBDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
}
