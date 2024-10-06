package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
    Optional<AppUser> findAppUserById(int id);

    Optional<AppUser> findAppUserByUsername(String username);

    Optional<AppUser> findAppUserByEmail(String email);
}
