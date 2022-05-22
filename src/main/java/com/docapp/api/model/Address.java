package com.docapp.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "street")
	private String street;
	@Column(name = "house_number")
	private String houseNumber;
	@Column(name = "city")
	private String city;
	@Column(name = "zip_code")
	private String zipCode;
	@Column(name = "additional_info")
	private String additionalInfo;
}
