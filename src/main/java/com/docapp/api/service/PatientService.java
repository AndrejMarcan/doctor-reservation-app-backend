package com.docapp.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.docapp.api.model.Account;
import com.docapp.api.model.Address;
import com.docapp.api.model.Appointment;
import com.docapp.api.model.Patient;
import com.docapp.api.model.PatientSearchRequest;
import com.docapp.api.repository.AppointmentRepository;
import com.docapp.api.repository.PatientRepository;


@Service
@EnableScheduling
public class PatientService {
	private final PatientRepository patientRepository;
	private final AccountService accountService;
	private final AddressService addressService;
	private final AppointmentRepository appointmentRepository;

	public PatientService(@Autowired PatientRepository patientRepository, @Autowired AccountService accountService,
			@Autowired AddressService addressService, @Autowired AppointmentRepository appointmentRepository ) {
		super();
		this.patientRepository = patientRepository;
		this.accountService = accountService;
		this.addressService = addressService;
		this.appointmentRepository = appointmentRepository;
	}
	
	/** Create new patient record */
	public Patient createPatient(Patient newPatientRequest) {
		Patient patient = patientRepository.save(newPatientRequest);
		Account account = accountService.createAccountFromPatient(patient);
		patient.setAccountId(account.getId());
		return patientRepository.save(patient);
	}

	/** Get full patient detail */
	public Patient getPatientDetail(Long id) {
		/** Fetch data from DB */
		Optional<Patient> patient = patientRepository.findById(id);
		if(patient.isEmpty()) {
			return null;
		}
		return patient.get();
	}

	/** Update patient data */
	public Patient updatePatient(Long id, Patient request) {
		Optional<Patient> patient = patientRepository.findById(id);
		Patient patientToUpdate = patient.get();
		
		if(request.getBirthNumber() != null && (request.getBirthNumber() < 1000000000L || request.getBirthNumber() > 9999999999L)) {
			patientToUpdate.setBirthNumber(request.getBirthNumber());
		}
		
		if(!StringUtils.isBlank(request.getFirstname())) {
			patientToUpdate.setFirstname(request.getFirstname());
		}
		
		if(!StringUtils.isBlank(request.getSurename())) {
			patientToUpdate.setSurename(request.getSurename());
		}
		
		if(!StringUtils.isBlank(request.getEmail())) {
			patientToUpdate.setEmail(request.getEmail());
		}
		
		if(!StringUtils.isBlank(request.getNote())) {
			patientToUpdate.setNote(request.getNote());
		}
		
		if(request.getAddress() != null) {
			Address updated = addressService.updateAddress(patientToUpdate.getAddress().getId(), request.getAddress());
			patientToUpdate.setAddress(updated);
		}
		
		return patientRepository.save(patientToUpdate);
	}
	
	/** Delete Patient by id*/
	public boolean deletePatient(Long id) {
		Optional<Patient> patient = patientRepository.findById(id);
		
		if(patient.isEmpty()) {
			return true;
		}
		/** Delete account */
		accountService.deleteByPatientId(id);
		/** Delete patient */
		boolean result = patientRepository.deleteByPatientId(id) > 0;
		/** Delete address */
		if(patient.get().getAddress() != null) {
			addressService.deleteAddressById(patient.get().getAddress().getId());
		}
		/** Delete appointments */
		appointmentRepository.deleteByAppointmentbyPatientId(id);
		return result;
	}

	/** Get All patients */
	public List<Patient> getPatients() {
		List<Patient> listOfPatients = patientRepository.findAll();
		return listOfPatients;
	}
	
	/** Get Patients by filter */
	public List<Patient> getPatientsByRequest(PatientSearchRequest request) {
		List<Patient> listOfPatients = new ArrayList<>();
		
		if(!StringUtils.isBlank(request.getFirstname()) && !StringUtils.isBlank(request.getSurename())) {
			return patientRepository.findPatientsByFirstnameAndSurename(request.getFirstname(), request.getSurename());
		}
		
		if(request.getBirthNumber() != null) {
			return patientRepository.findPatientsByBirthNumber(request.getBirthNumber());
		}
		
		if(!StringUtils.isBlank(request.getFirstname())) {
			return patientRepository.findPatientsByFirstname(request.getFirstname());
		}
		
		if(!StringUtils.isBlank(request.getSurename())) {
			return patientRepository.findPatientsBySurename(request.getSurename());
		}
		
		return listOfPatients;
	}
	
	public Patient getByUsername(String username) {
		Account acc = accountService.getAccountByUsername(username);
		
		if(acc == null) {
			return null;
		}
		
		return acc.getPatient();
	}	
	
	@Scheduled(cron = "0 0 8 * * ?")
	private void sendEmails() throws MessagingException {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "*");
		
		Calendar calendar = new GregorianCalendar();
		calendar.roll(Calendar.DAY_OF_MONTH, true); // go to tmrw
		
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
		
		List<Appointment> appointments = appointmentRepository.findAppointmentsBetween(dateFrom, dateTo);
		
		List<Long> patientIds = appointments.stream().map(a -> a.getPatientId()).collect(Collectors.toList());
		
		List<Patient> patients = patientRepository.findByIdIn(patientIds);
		
		for(Patient patient : patients) {
			Session session = Session.getInstance(prop, new Authenticator() {
			    @Override
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication("testovacia.adresa1@gmail.com", "Testovacia_1");
			    }
			});
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("testovacia.adresa1@gmail.com"));
			message.setRecipients(
			  Message.RecipientType.TO, InternetAddress.parse(patient.getEmail()));
			message.setSubject("Stretnutie Ambulancia Bernie");

			String msg = "Pripominame Vam termin stretnutia v nasej ambulancii.";

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);

			Transport.send(message);
		}
	}
	
}
