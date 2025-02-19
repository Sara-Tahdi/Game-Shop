package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends CrudRepository<Staff, Integer> {
    Optional<Staff> findStaffById(int id);

    Optional<Staff> findStaffByUsername(String username);

    Optional<Staff> findStaffByEmail(String email);

    @Query("SELECT a FROM AppUser a WHERE TYPE(a) = :type")
    Optional<List<Staff>> findStaffByUserType(@Param("type") Class<?> type);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isActive = false WHERE a.username = :username")
    void updateByUsername(@Param("username") String username);
}
