package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentInfoRepository extends CrudRepository<PaymentInfo, Integer> {

    Optional<PaymentInfo> findPaymentInfoById(int id);

    Optional<PaymentInfo> findPaymentInfoByCardNumber(String aCardNumber);

    @Query("select p from PaymentInfo p where p.client.id = :clientId")
    Optional<List<PaymentInfo>> findPaymentInfosByClientId(@Param("clientId") int clientId);
}
