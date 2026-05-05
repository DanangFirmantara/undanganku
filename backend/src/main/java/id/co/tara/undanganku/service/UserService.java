package id.co.tara.undanganku.service;

import id.co.tara.undanganku.domain.Role;
import id.co.tara.undanganku.domain.User;
import id.co.tara.undanganku.dto.UserCreateRequest;
import id.co.tara.undanganku.dto.UserResponse;
import id.co.tara.undanganku.exception.ResourceNotFoundException;
import id.co.tara.undanganku.repository.RoleRepository;
import id.co.tara.undanganku.repository.UserRepository;
import id.co.tara.undanganku.security.PasswordUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordUtil passwordUtil;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordUtil passwordUtil) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordUtil = passwordUtil;
	}

	public List<UserResponse> getAllUsers() {
		return userRepository.findAllActive().stream()
			.map(UserResponse::new)
			.collect(Collectors.toList());
	}

	public Page<UserResponse> getAllUsers(Pageable pageable) {
		return userRepository.findAllActive(pageable)
			.map(UserResponse::new);
	}

	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		if (user.isDeleted()) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		return new UserResponse(user);
	}

	public UserResponse getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
		return new UserResponse(user);
	}

	@Transactional
	public UserResponse createUser(UserCreateRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already exists: " + request.getEmail());
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setPasswordHash(passwordUtil.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setIsActive(true);

		Set<Role> roles = new HashSet<>();
		for (String roleName : request.getRoles()) {
			Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
			roles.add(role);
		}
		user.setRoles(roles);

		User savedUser = userRepository.save(user);
		return new UserResponse(savedUser);
	}

	@Transactional
	public UserResponse updateUser(Long id, UserCreateRequest request) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());

		Set<Role> roles = new HashSet<>();
		for (String roleName : request.getRoles()) {
			Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
			roles.add(role);
		}
		user.setRoles(roles);

		User updatedUser = userRepository.save(user);
		return new UserResponse(updatedUser);
	}

	@Transactional
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		user.softDelete();
		userRepository.save(user);
	}

	public Page<UserResponse> searchUsers(String search, Pageable pageable) {
		return userRepository.searchByEmail(search, pageable)
			.map(UserResponse::new);
	}
}
