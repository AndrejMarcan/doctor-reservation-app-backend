package com.docapp.api.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docapp.api.model.Address;
import com.docapp.api.repository.AddressRepository;

@Service
public class AddressService {
	private AddressRepository addressRepository;
	
	public AddressService(@Autowired AddressRepository addressRepository) {
		super();
		this.addressRepository = addressRepository;
	}
	
	/** Get Address Details */
	public Address getAddressDetails(Long id) {
		Optional<Address> address = addressRepository.findById(id);
		
		if(address.isEmpty()) {
			return null;
		}
		
		return address.get();
	}
	
	/** Update Address */
	public Address updateAddress(Long id, Address request) {
		Optional<Address> address = addressRepository.findById(id);
		
		if(address.isEmpty()) {
			return null;
		}
		
		Address addressToUpdate = address.get();
		
		if(!StringUtils.isBlank(request.getAdditionalInfo())) {
			addressToUpdate.setAdditionalInfo(request.getAdditionalInfo());
		}
		
		if(!StringUtils.isBlank(request.getCity())) {
			addressToUpdate.setCity(request.getCity());
		}
		
		if(!StringUtils.isBlank(request.getHouseNumber())) {
			addressToUpdate.setHouseNumber(request.getHouseNumber());
		}
		
		if(!StringUtils.isBlank(request.getStreet())) {
			addressToUpdate.setStreet(request.getStreet());
		}
		
		if(!StringUtils.isBlank(request.getZipCode())) {
			addressToUpdate.setZipCode(request.getZipCode());
		}
		
//		return addressRepository.save(addressToUpdate);
		return addressToUpdate;
	}
	
	/** Delete address only used by delete patient method */
	public void deleteAddressById(Long id) {
		addressRepository.deleteByAddressId(id);
	}
}
