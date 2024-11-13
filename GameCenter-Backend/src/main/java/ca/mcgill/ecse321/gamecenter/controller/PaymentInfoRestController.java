package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.service.AppUserService;
import ca.mcgill.ecse321.gamecenter.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PaymentInfoRestController {
    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private AppUserService appUserService;

    @PostMapping("/paymentInfo/{clientId}")
    public PaymentInfoResponseDTO addPaymentInfo(@Validated @RequestBody PaymentInfoRequestDTO req, @PathVariable int clientId) {
        Client client = appUserService.getClientById(clientId);
        return new PaymentInfoResponseDTO(paymentInfoService.savePaymentInfo(req.getCardNumber(), req.getCvv(), req.getExpiryMonth(), req.getExpiryYear(), client));
    }

    @DeleteMapping("/paymentInfo/{paymentInfoId}")
    public void deletePaymentInfo(@PathVariable int paymentInfoId) {
        paymentInfoService.deletePaymentInfo(paymentInfoId);
    }

    @GetMapping("/paymentInfo/{clientId}")
    public List<PaymentInfoResponseDTO> getPaymentInfosByClient(@PathVariable int clientId) {
        return paymentInfoService.getPaymentInfosByClient(clientId).stream().map(
                p -> new PaymentInfoResponseDTO(p))
                .collect(Collectors.toList());
    }
}
