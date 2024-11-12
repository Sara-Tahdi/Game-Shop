package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;
import ca.mcgill.ecse321.gamecenter.repository.PaymentInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentInfoService {
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    public PaymentInfo findPaymentInfoById(int id) {
        PaymentInfo paymentInfo = paymentInfoRepository.findPaymentInfoById(id).orElse(null);
        if (paymentInfo == null) {throw new IllegalArgumentException("No payment info with id: " + id);}
        return paymentInfo;
    }

    @Transactional
    public PaymentInfo savePaymentInfo(
        String cardNumber,
        String cvv,
        Integer expiryMonth,
        Integer expiryYear,
        Client client)
    {
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, client);

        PaymentInfo paymentInfoFromRepo = paymentInfoRepository.findPaymentInfoByCardNumber(cardNumber).orElse(null);
        if (paymentInfoFromRepo != null) {
            throw new IllegalArgumentException("A payment info with card number "+cardNumber+" already exists in the system.");
        }
        return paymentInfoRepository.save(paymentInfo);
    }

    @Transactional
    public void deletePaymentInfo(int paymentInfoId) {
        PaymentInfo paymentInfo = paymentInfoRepository.findPaymentInfoById(paymentInfoId).orElse(null);
        if (paymentInfo == null) {throw new IllegalArgumentException("No payment info with id: " + paymentInfoId);}
        paymentInfoRepository.delete(paymentInfo);
    }

    public List<PaymentInfo> getPaymentInfosByClient(int clientId) {
        List<PaymentInfo> paymentInfos = paymentInfoRepository.findPaymentInfosByClientId(clientId).orElse(null);
        if (paymentInfos == null) {throw new IllegalArgumentException("Client: " + clientId + "has no payment infos");}
        return paymentInfos;
    }

}
