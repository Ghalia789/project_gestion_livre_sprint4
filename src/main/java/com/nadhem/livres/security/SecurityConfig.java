package com.nadhem.livres.security;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private javax.sql.DataSource dataSource;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
	throws Exception {
		PasswordEncoder passwordEncoder = passwordEncoder ();
		System.out.println("Passwors Encoded BCRYPT :******************** ");
		System.out.println(passwordEncoder.encode("123"));
		
		auth.jdbcAuthentication().dataSource(dataSource)
		 .usersByUsernameQuery("select username , password , enabled from user where username =?")
		 .authoritiesByUsernameQuery(
		 "SELECT u.username, r.role " +
		 "FROM user_role ur, user u , role r " +
		 "WHERE u.user_id = ur.user_id AND ur.role_id = r.role_id AND u.username = ?") 
		 .passwordEncoder(passwordEncoder)
		 .rolePrefix("ROLE_");
	
	}
	 protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests().antMatchers("/showCreate").hasAnyRole("ADMIN","AGENT");
		 http.authorizeRequests().antMatchers("/saveLivre").hasAnyRole("ADMIN","AGENT");
		 http.authorizeRequests().antMatchers("/ListeLivres")
		 .hasAnyRole("ADMIN","AGENT","USER");
		 
		 http.authorizeRequests()
		 .antMatchers("/supprimerLivre","/modifierLivre","/updateLivre")
		 .hasAnyRole("ADMIN");
		 
		 http.authorizeRequests().anyRequest().authenticated();
		 http.formLogin();
		 }
	 
	@Bean
	 public PasswordEncoder passwordEncoder () {
	 return new BCryptPasswordEncoder();
	 }
}
