package com.docapp.api.model;

import java.util.List;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private final String jwt;
	private final String username;
	private final List<String> role;
	
	public AuthenticationResponse(String jwt, String username, List<String> role){
		this.jwt = jwt;
		this.username = username;
		this.role = role;
	}
}
