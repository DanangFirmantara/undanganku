package id.co.tara.undanganku.controller;

import id.co.tara.undanganku.dto.UserCreateRequest;
import id.co.tara.undanganku.dto.UserResponse;
import id.co.tara.undanganku.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping(params = "page")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
		Page<UserResponse> users = userService.getAllUsers(pageable);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		UserResponse user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/email/{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
		UserResponse user = userService.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
		UserResponse user = userService.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> updateUser(
		@PathVariable Long id,
		@Valid @RequestBody UserCreateRequest request) {
		UserResponse user = userService.updateUser(id, request);
		return ResponseEntity.ok(user);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<UserResponse>> searchUsers(
		@RequestParam String search,
		Pageable pageable) {
		Page<UserResponse> users = userService.searchUsers(search, pageable);
		return ResponseEntity.ok(users);
	}
}
