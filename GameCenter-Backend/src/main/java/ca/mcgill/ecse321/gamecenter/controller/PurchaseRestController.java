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

    @PutMapping("/purchases/refund/{trackingCode}")
    public ResponseEntity<?> refundPurchase(@Validated @RequestBody RefundRequestDTO refundReason, @PathVariable String trackingCode) {
        try {
            SimplePurchaseResponseDTO simplePurchase = purchaseService.returnGame(trackingCode, refundReason.getReason());
            return ResponseEntity.ok().body(simplePurchase);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/purchases/{clientId}")
    public ResponseEntity<?> getPurchaseHistory(@PathVariable int clientId) {
        try {
            List<Purchase> purchases = purchaseService.getClientPurchaseHistory(clientId);
            List<SimplePurchaseResponseDTO> simplePurchases = buildSimplePurchases(purchases);
            return ResponseEntity.ok().body(simplePurchases);
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

    private List<SimplePurchaseResponseDTO> buildSimplePurchases(List<Purchase> purchases) {
        HashMap<String, Float> priceMap = new HashMap<>();
        HashMap<String, String> refundMap = new HashMap<>();
        for (Purchase p: purchases) {
            String trackingCode = p.getTrackingCode();
            String refundReason = p.getRefundReason();
            Float purchaseTotal = priceMap.getOrDefault(trackingCode, 0f);
            priceMap.put(trackingCode, Round.round(purchaseTotal + p.getTotalPrice()));
            refundMap.putIfAbsent(trackingCode, refundReason);
        }
        return priceMap.entrySet().stream()
                .map(entry -> new SimplePurchaseResponseDTO(entry.getKey(), entry.getValue(), refundMap.get(entry)))
                .collect(Collectors.toList());
    }
}
