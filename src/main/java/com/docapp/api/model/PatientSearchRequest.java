package com.docapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientSearchRequest {
	private Long birthNumber;
	private String firstname;
	private String surename;
}
