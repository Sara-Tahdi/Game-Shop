package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.Request;
import ca.mcgill.ecse321.gamecenter.model.GameRequest;
import ca.mcgill.ecse321.gamecenter.model.UserRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends CrudRepository<Request, Integer> {
    Optional<Request> findRequestById(int id);

    @Query("SELECT r FROM Request r WHERE TYPE(r) = :type")
    Optional<List<Request>> findRequestsByRequestType(@Param("type") Class<?> type);

    Optional<List<Request>> findRequestsByCreatedRequestId(int createdRequestId);

    Optional<List<Request>> findRequestsByCreatedRequestUsername(String username);

    Optional<List<Request>> findRequestsByCreatedRequestEmail(String email);

    Optional<List<Request>> findRequestsByStatus(Request.Status status);

    Optional<List<GameRequest>> findRequestsByGameTitle(String gameTitle);

    Optional<List<GameRequest>> findRequestsByGameId(int gameId);

    Optional<List<UserRequest>> findRequestsByUserFacingJudgementId(int userFacingJudgementId);

    Optional<List<UserRequest>> findRequestsByUserFacingJudgementUsername(String username);

    Optional<List<UserRequest>> findRequestsByUserFacingJudgementEmail(String email);

}
