package com.docapp.api.validator;

import com.docapp.api.model.Appointment;

public class AppointmentValidator extends BaseValidator {
	public static String MISSING_APPOINTMENT_DATE_ERROR_MESSAGE = "Missing 'date_of_appointment' parameter";
	public static String MISSING_REASON_ERROR_MESSAGE = "Missing 'reason' parameter";
	
	public void validateCreateAppointmentRequest(Appointment request) {
		
	}
}
