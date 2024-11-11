package ca.mcgill.ecse321.gamecenter.controller;

import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.RefundRequestDTO;
import ca.mcgill.ecse321.gamecenter.service.PurchaseService;
import ca.mcgill.ecse321.gamecenter.utilities.TrackingCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PurchaseRestController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/purchases/place/{clientId}")
    public List<PurchaseResponseDTO> createPurchases(@Validated @RequestBody List<PurchaseRequestDTO> purchases, @PathVariable int clientId) {
        return purchaseService.createPurchases(purchases, clientId).stream()
                .map(p -> new PurchaseResponseDTO(p))
                .collect(Collectors.toList());
    }

    @PutMapping("/purchases/refund/{purchaseId}")
    public PurchaseResponseDTO refundPurchase(@Validated @RequestBody RefundRequestDTO refundReason, @PathVariable int purchaseId) {
        return new PurchaseResponseDTO(purchaseService.returnGame(purchaseId, refundReason.getReason()));
    }

    @GetMapping("/purchases/{clientId}")
    public List<PurchaseResponseDTO> getPurchaseHistory(@PathVariable int clientId) {
        return purchaseService.getClientPurchaseHistory(clientId).stream()
                .map(p -> new PurchaseResponseDTO(p))
                .collect(Collectors.toList());
    }

    @GetMapping("/purchases/recent/{clientId}")
    public List<PurchaseResponseDTO> getPurchaseHistory90Days(@PathVariable int clientId) {
        return purchaseService.getClientPurchaseHistory90Days(clientId).stream()
                .map(p -> new PurchaseResponseDTO(p))
                .collect(Collectors.toList());
    }

    @GetMapping("/purchases/group/{trackingCode}")
    public List<PurchaseResponseDTO> getPurchaseByTrackingCode(@PathVariable String trackingCode) {
        return purchaseService.getPurchaseByTrackingCode(trackingCode).stream()
                .map(p -> new PurchaseResponseDTO(p))
                .collect(Collectors.toList());
    }
}
