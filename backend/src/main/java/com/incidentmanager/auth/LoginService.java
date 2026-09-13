//AuthenticationService.java
package com.incidentmanager.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.incidentmanager.config.JwtServiceGenerator;
import com.incidentmanager.entity.User;
import com.incidentmanager.repository.UserRepository;

@Service
public class LoginService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;


	
	public String logar(Login login) {

		String token = this.gerarToken(login);
		return token;

	}



	public String gerarToken(Login login) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername(),
						login.getPassword()
						)
				);
		User user = repository.findByLogin(login.getUsername()).get();
		String jwtToken = jwtService.generateToken(user);
		return jwtToken;
	}


}
