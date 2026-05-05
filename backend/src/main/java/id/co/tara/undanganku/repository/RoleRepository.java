package id.co.tara.undanganku.repository;

import id.co.tara.undanganku.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.name = :name AND r.deletedAt IS NULL")
	Optional<Role> findByName(@Param("name") String name);
}
