package ca.mcgill.ecse321.gamecenter.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.gamecenter.model.AppUser;
public interface ExampleCRUDRepository extends CrudRepository<AppUser, Integer> {
    AppUser findAppUserById(int id);
}
