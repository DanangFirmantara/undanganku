package id.co.tara.undanganku.repository;

import id.co.tara.undanganku.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
	Optional<User> findByEmail(@Param("email") String email);

	@Query("SELECT u FROM User u WHERE u.guid = :guid AND u.deletedAt IS NULL")
	Optional<User> findByGuid(@Param("guid") UUID guid);

	@Query("SELECT u FROM User u WHERE u.deletedAt IS NULL ORDER BY u.createdAt DESC")
	List<User> findAllActive();

	@Query("SELECT u FROM User u WHERE u.deletedAt IS NULL ORDER BY u.createdAt DESC")
	Page<User> findAllActive(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.isActive = true AND u.deletedAt IS NULL")
	List<User> findAllActiveUsers();

	@Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%', :search, '%') AND u.deletedAt IS NULL")
	Page<User> searchByEmail(@Param("search") String search, Pageable pageable);
}
