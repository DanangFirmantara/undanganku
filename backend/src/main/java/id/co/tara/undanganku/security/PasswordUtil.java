package id.co.tara.undanganku.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public String encode(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}

	public boolean isValidPassword(String password) {
		return password != null && password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*[0-9].*");
	}
}
