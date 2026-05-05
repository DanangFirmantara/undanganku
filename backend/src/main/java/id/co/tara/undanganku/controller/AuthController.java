package id.co.tara.undanganku.controller;

import id.co.tara.undanganku.domain.Role;
import id.co.tara.undanganku.domain.User;
import id.co.tara.undanganku.dto.LoginRequest;
import id.co.tara.undanganku.dto.LoginResponse;
import id.co.tara.undanganku.repository.RoleRepository;
import id.co.tara.undanganku.repository.UserRepository;
import id.co.tara.undanganku.security.PasswordUtil;
import id.co.tara.undanganku.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordUtil passwordUtil;

	public AuthController(AuthService authService, UserRepository userRepository, RoleRepository roleRepository, PasswordUtil passwordUtil) {
		this.authService = authService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordUtil = passwordUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		LoginResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		Map<String, String> response = new HashMap<>();
		response.put("message", "Logout successful");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("valid", false, "message", "No token provided"));
		}

		String jwt = token.substring(7);
		boolean isValid = authService.validateToken(jwt);

		if (isValid) {
			String email = authService.extractEmailFromToken(jwt);
			return ResponseEntity.ok(Map.of("valid", true, "email", email));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("valid", false, "message", "Invalid or expired token"));
		}
	}

	@PostMapping("/init-admin")
	public ResponseEntity<?> initAdmin() {
		if (userRepository.findByEmail("admin@app.com").isPresent()) {
			return ResponseEntity.ok(Map.of("message", "Admin user already exists"));
		}

		Role adminRole = roleRepository.findByName("ROLE_ADMIN")
			.orElseGet(() -> {
				Role role = new Role();
				role.setGuid(UUID.randomUUID());
				role.setName("ROLE_ADMIN");
				role.setDescription("Administrator with full access");
				role.setCreatedBy("system");
				role.setUpdatedBy("system");
				return roleRepository.save(role);
			});

		User adminUser = new User();
		adminUser.setGuid(UUID.randomUUID());
		adminUser.setEmail("admin@app.com");
		adminUser.setPasswordHash(passwordUtil.encode("Admin@123456"));
		adminUser.setFirstName("Admin");
		adminUser.setLastName("User");
		adminUser.setIsActive(true);
		adminUser.setCreatedBy("system");
		adminUser.setUpdatedBy("system");
		adminUser.setRoles(new HashSet<>());
		adminUser.getRoles().add(adminRole);

		userRepository.save(adminUser);
		return ResponseEntity.ok(Map.of("message", "Admin user created successfully", "email", "admin@app.com"));
	}
}
