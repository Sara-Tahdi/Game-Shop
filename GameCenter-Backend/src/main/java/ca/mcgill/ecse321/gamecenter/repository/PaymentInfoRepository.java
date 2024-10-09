package ca.mcgill.ecse321.gamecenter.repository;

import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;
import org.springframework.data.repository.CrudRepository;

public interface PaymentInfoRepository extends CrudRepository<PaymentInfo, Integer> {
}
