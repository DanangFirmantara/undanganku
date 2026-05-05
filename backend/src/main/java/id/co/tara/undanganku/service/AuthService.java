package id.co.tara.undanganku.service;

import id.co.tara.undanganku.domain.User;
import id.co.tara.undanganku.dto.LoginRequest;
import id.co.tara.undanganku.dto.LoginResponse;
import id.co.tara.undanganku.dto.UserResponse;
import id.co.tara.undanganku.exception.AuthenticationException;
import id.co.tara.undanganku.repository.UserRepository;
import id.co.tara.undanganku.security.JwtUtil;
import id.co.tara.undanganku.security.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordUtil passwordUtil;

	public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordUtil passwordUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.passwordUtil = passwordUtil;
	}

	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new AuthenticationException("Invalid email or password"));

		if (!user.getIsActive()) {
			throw new AuthenticationException("User account is inactive");
		}

		if (!passwordUtil.matches(request.getPassword(), user.getPasswordHash())) {
			throw new AuthenticationException("Invalid email or password");
		}

		String token = jwtUtil.generateToken(user.getEmail());
		UserResponse userResponse = new UserResponse(user);

		return new LoginResponse(token, jwtUtil.getExpirationTimeInMillis(), userResponse);
	}

	public boolean validateToken(String token) {
		return jwtUtil.validateToken(token);
	}

	public String extractEmailFromToken(String token) {
		return jwtUtil.extractEmail(token);
	}
}
