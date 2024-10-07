package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
    Optional<AppUser> findAppUserById(int id);

    Optional<AppUser> findAppUserByUsername(String username);

    Optional<AppUser> findAppUserByEmail(String email);

    @Query("SELECT a FROM AppUser a WHERE TYPE(a) = :type")
    Optional<List<AppUser>> findAppUserByUserType(@Param("type") Class<?> type);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isActive = false WHERE a.username = :username")
    void updateByUsername(@Param("username") String username);
}
