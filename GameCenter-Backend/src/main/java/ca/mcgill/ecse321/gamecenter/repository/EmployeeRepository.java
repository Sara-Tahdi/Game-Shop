package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import ca.mcgill.ecse321.gamecenter.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Optional<Employee> findEmployeeById(int id);

    Optional<Employee> findEmployeeByUsername(String username);

    Optional<Employee> findEmployeeByEmail(String email);

    @Query("SELECT a FROM AppUser a WHERE TYPE(a) = :type")
    Optional<List<Employee>> findEmployeeByUserType(@Param("type") Class<?> type);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isActive = false WHERE a.username = :username")
    void updateByUsername(@Param("username") String username);
}
