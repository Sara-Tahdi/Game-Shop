package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface PaymentInfoRepository extends CrudRepository<PaymentInfo, Integer> {

    Optional<PaymentInfo> findPaymentInfoById(int id);

    Optional<PaymentInfo> findPaymentInfoByCardNumber(String aCardNumber);
}
