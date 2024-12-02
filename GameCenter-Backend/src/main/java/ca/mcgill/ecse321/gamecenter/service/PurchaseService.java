package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.dto.Purchase.PurchaseRequestDTO;
import ca.mcgill.ecse321.gamecenter.dto.Purchase.SimplePurchaseResponseDTO;
import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import ca.mcgill.ecse321.gamecenter.utilities.Round;
import ca.mcgill.ecse321.gamecenter.utilities.TrackingCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;

    public List<Purchase> createPurchases(List<PurchaseRequestDTO> purchases, int clientId) {
        String trackingCode = TrackingCode.nextCode();
        while (!purchaseRepository.findPurchasesByTrackingCode(trackingCode).orElse(List.of()).isEmpty()) {
            trackingCode = TrackingCode.nextCode();
        }
        for (PurchaseRequestDTO p: purchases) {
            createPurchase(clientId, p.getGameId(), p.getCopies(), trackingCode);
        }
        return getPurchaseByTrackingCode(trackingCode);
    }

    public Purchase createPurchase(int clientId, int gameId, int aCopies, String trackingCode) {
        Client c = (Client) appUserRepository.findAppUserById(clientId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("There is no Client with id: " + clientId);
        }

        Game g = gameRepository.findGameById(gameId).orElse(null);
        if (g == null) {
            throw new IllegalArgumentException("There is no Game with id: " + gameId);
        }

        if (aCopies > g.getRemainingQuantity()) {
            throw new IllegalArgumentException("Attempting to purchase more copies than available, copies left: " + g.getRemainingQuantity());
        }

        float total = Round.round(g.getPrice() * aCopies);
        int copies = aCopies;
        Date purchaseDate = Date.valueOf(LocalDate.now());

        Purchase p = new Purchase(total, copies, trackingCode, purchaseDate, g, c);
        g.setRemainingQuantity(g.getRemainingQuantity() - copies);
        gameRepository.save(g);
        return purchaseRepository.save(p);
    }

    public Purchase returnGame(int purchaseId, String refundReason) {
        Purchase purchase = purchaseRepository.findPurchaseById(purchaseId).orElse(null);
        if (purchase == null) {
            throw new IllegalArgumentException("There is no Purchase with id: " + purchaseId);
        }

        // check if refund request is within valid time
        if (purchase.getPurchaseDate().toLocalDate().isBefore(LocalDate.now().minusDays(7))) {
            throw new IllegalArgumentException("Refund request DENIED!! Refund period is over.");
        }

        purchase.setRefundReason(refundReason);
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getClientPurchaseHistory(int clientId) {
        Client c = (Client) appUserRepository.findAppUserById(clientId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("There is no Client with id: " + clientId);
        }

        List<Purchase> purchases = purchaseRepository.findPurchasesByClientId(clientId).orElse(null);
        if (purchases == null) {
            throw new IllegalArgumentException("Client with id " + clientId + " has no purchases");
        }
        return purchases;
    }

    public List<Purchase> getPurchaseByTrackingCode(String trackingCode) {
        return purchaseRepository.findPurchasesByTrackingCode(trackingCode).orElse(List.of());
    }
}