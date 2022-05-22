package com.docapp.api.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.docapp.api.model.Account;
import com.docapp.api.repository.AccountRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> acc = accountRepository.findByUsername(username);
		
		acc.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		return new User(acc.get().getUsername(), acc.get().getPassword(), Arrays.stream(acc.get().getRole().split(","))
				 .map(SimpleGrantedAuthority::new)
				 .collect(Collectors.toList()));
	}

}
