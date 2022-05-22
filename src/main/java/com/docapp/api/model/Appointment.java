package com.docapp.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "patient_id")
    private Long patientId;
	@Column(name = "date_of_appointment")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfAppointment;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_creation")
	private Date dateOfCreation;
	@Column(name = "reason")
	private String reason;
	@Transient
	private String time;
	@Transient
	private String date;
}
