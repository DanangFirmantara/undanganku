package id.co.tara.undanganku.security;

import id.co.tara.undanganku.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

	private final JwtConfig jwtConfig;
	private final SecretKey key;

	public JwtUtil(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
		this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
	}

	public String generateToken(String email) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

		return Jwts.builder()
			.subject(email)
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public String extractEmail(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Claims getClaimsFromToken(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public long getExpirationTimeInMillis() {
		return jwtConfig.getExpiration();
	}
}
