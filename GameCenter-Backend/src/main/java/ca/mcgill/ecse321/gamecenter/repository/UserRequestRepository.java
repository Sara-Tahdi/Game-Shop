package ca.mcgill.ecse321.gamecenter.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;


public interface UserRequestRepository extends CrudRepository<UserRequest, Integer> {
    public UserRequest findUserRequestById(int id);
}
