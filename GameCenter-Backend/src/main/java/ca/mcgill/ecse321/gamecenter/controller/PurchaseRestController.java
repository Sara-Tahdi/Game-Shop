package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.RefundRequestDTO;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.service.PurchaseService;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.SimplePurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.utilities.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PurchaseRestController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/purchases/place/{clientId}")
    public ResponseEntity<?> createPurchases(@Validated @RequestBody List<PurchaseRequestDTO> purchases, @PathVariable int clientId) {
        try {
            return ResponseEntity.ok().body(purchaseService.createPurchases(purchases, clientId).stream()
                    .map(p -> new PurchaseResponseDTO(p))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/purchases/refund/{purchaseId}")
    public ResponseEntity<?> refundPurchase(@Validated @RequestBody RefundRequestDTO refundReason, @PathVariable int purchaseId) {
        try {
            Purchase purchase = purchaseService.returnGame(purchaseId, refundReason.getRefundReason());
            return ResponseEntity.ok().body(new PurchaseResponseDTO(purchase));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/purchases/{clientId}")
    public ResponseEntity<?> getPurchaseHistory(@PathVariable int clientId) {
        try {
            List<Purchase> purchases = purchaseService.getClientPurchaseHistory(clientId);
            return ResponseEntity.ok().body(purchases);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/purchases/group/{trackingCode}")
    public ResponseEntity<?> getPurchaseByTrackingCode(@PathVariable String trackingCode) {
        try {
            return ResponseEntity.ok().body(purchaseService.getPurchaseByTrackingCode(trackingCode).stream()
                    .map(p -> new PurchaseResponseDTO(p))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
