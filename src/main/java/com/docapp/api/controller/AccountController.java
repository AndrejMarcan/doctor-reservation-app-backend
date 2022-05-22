package com.docapp.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.docapp.api.model.Account;
import com.docapp.api.model.AuthenticationRequest;
import com.docapp.api.model.AuthenticationResponse;
import com.docapp.api.service.AccountService;
import com.docapp.api.service.AppUserDetailsService;
import com.docapp.api.utils.JwtUtils;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class AccountController {
	
	private AccountService accountService;
	private AuthenticationManager authenticationManager;
	private AppUserDetailsService userDetailsService;
	private JwtUtils jwtUtil;
	
	public AccountController(@Autowired AccountService accountService, @Autowired AuthenticationManager authenticationManager,
			@Autowired AppUserDetailsService userDetailsService,@Autowired JwtUtils jwtUtil) {
		super();
		this.accountService = accountService;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}
	
	@PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> createAccount(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@RequestBody Account request) {
		/** Call create method in AccountService */
		return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> getAccountById(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id) {
		Account account = accountService.getAccountById(id);
		
		if(account == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> updateAccountById(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id, @RequestBody Account request) {
		Account account = accountService.updateAccountById(id, request);
		
		if(account == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/account/{id}")
	public ResponseEntity<Void> deleteAccount(@RequestHeader(value = "Authorization", required = true) String authorization, 
			@PathVariable("id") Long id) {
		accountService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/accounts/doctors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Account>> getAccountById(@RequestHeader(value = "Authorization", required = true) String authorization) {
		List<Account> accounts = accountService.getDoctors();
		return new ResponseEntity<>(accounts, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()) //standard token
			);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt, userDetails.getUsername(), roles));
	}
}
