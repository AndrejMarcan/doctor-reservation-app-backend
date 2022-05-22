package com.docapp.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import com.docapp.api.filter.CorsFilter;
import com.docapp.api.filter.JwtRequestFilter;
import com.docapp.api.service.AppUserDetailsService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {	
	private static final String[] PATIENT_AUTHORITIES =
			new String[] {"ADMIN_ROLE", "PATIENT_ROLE", "DOCTOR_ROLE"};
	private static final String[] DOCTOR_AUTHORITIES =
			new String[] {"ADMIN_ROLE", "DOCTOR_ROLE"};
	private static final String[] ADMIN_AUTHORITIES =
			new String[] {"ADMIN_ROLE"};
	
	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appUserDetailsService);
	}
	
	@Bean
	protected CorsFilter corsHeaderFilter() {
		CorsFilter filter = new CorsFilter();
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/** Disable CSRF */
		http.addFilterBefore(corsHeaderFilter(), SessionManagementFilter.class).csrf().disable()
			/** Allow access to login API to all */
			.authorizeRequests().antMatchers("/api/v1/login").permitAll()
			/** Account */
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/account").hasAnyAuthority(ADMIN_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/account/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/account/*").hasAnyAuthority(ADMIN_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/accounts/doctors").hasAnyAuthority(ADMIN_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/account/*").hasAnyAuthority(DOCTOR_AUTHORITIES)
			/** Patient */
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/patient").hasAnyAuthority(DOCTOR_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/patient/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/patients").hasAnyAuthority(DOCTOR_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/patient/username/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/patients/search").hasAnyAuthority(DOCTOR_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/patient/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/patient/*").hasAnyAuthority(DOCTOR_AUTHORITIES)
			/** Appointments */
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/appointment").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/appointment/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/appointments/past/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/appointments/future/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/appointments/today").hasAnyAuthority(DOCTOR_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/appointments/between").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/v1/appointment/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/appointment/*").hasAnyAuthority(PATIENT_AUTHORITIES)
			.and()
			.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();	
	}
}
