package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.PaymentInfo.PaymentInfoResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.service.AppUserService;
import ca.mcgill.ecse321.gamecenter.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentInfoRestController {
    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private AppUserService appUserService;

    @PostMapping("/paymentInfo/{clientId}")
    public ResponseEntity<?> addPaymentInfo(@Validated @RequestBody PaymentInfoRequestDTO req, @PathVariable int clientId) {
        try {
            Client client = appUserService.getClientById(clientId);
            return ResponseEntity.ok().body(new PaymentInfoResponseDTO(
                    paymentInfoService.savePaymentInfo(
                        req.getCardNumber(),
                        req.getCvv(),
                        req.getExpiryMonth(),
                        req.getExpiryYear(),
                        client)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/paymentInfo/{paymentInfoId}")
    public ResponseEntity<?> deletePaymentInfo(@PathVariable int paymentInfoId) {
        try {
            paymentInfoService.deletePaymentInfo(paymentInfoId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/paymentInfo/{clientId}")
    public ResponseEntity<?> getPaymentInfosByClient(@PathVariable int clientId) {
        try {
            return ResponseEntity.ok().body(paymentInfoService.getPaymentInfosByClient(clientId).stream().map(
                            p -> new PaymentInfoResponseDTO(p))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
