package com.incidentmanager.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.incidentmanager.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceGenerator {  

	public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	public static final int EXPIRATION_HOURS = 1;
	private final String secretKey;

	public JwtServiceGenerator(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey;
	}

	public Map<String, Object> gerarPayload(User usuario){
		
		Map<String, Object> payloadData = new HashMap<>();
		payloadData.put("username", usuario.getUsername());
		payloadData.put("id", usuario.getId().toString());
		payloadData.put("role", usuario.getRole().name());
		
		return payloadData;
	}	
	
	public String generateToken(User usuario) {

		Map<String, Object> payloadData = this.gerarPayload(usuario);

		return Jwts
				.builder()
				.setClaims(payloadData)
				.setSubject(usuario.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(new Date().getTime() + 3600000 * this.EXPIRATION_HOURS))
				.signWith(getSigningKey(), this.SIGNATURE_ALGORITHM)
				.compact();
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}


	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

}
