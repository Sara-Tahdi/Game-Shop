package ca.mcgill.ecse321.gamecenter.service;

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

    public Purchase createPurchase(int clientId, int gameId, int aCopies) {
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
        String trackingCode = TrackingCode.nextCode(); // avoid negative numbers
        Date purchaseDate = Date.valueOf(LocalDate.now());

        Purchase p = new Purchase(total, copies, trackingCode, purchaseDate, g, c);
        p = purchaseRepository.save(p);
        g.setRemainingQuantity(g.getRemainingQuantity() - copies);
        g = gameRepository.save(g);
        return p;
    }

    public Purchase returnGame(int purchaseId, String refundReason) {
        Purchase p = purchaseRepository.findPurchaseById(purchaseId).orElse(null);
        if (p == null) {
            throw new IllegalArgumentException("There is no Purchase with id: " + purchaseId);
        }

        // check if refund request is within valid time
        if (p.getPurchaseDate().toLocalDate().isBefore(LocalDate.now().minusDays(7))) {
            throw new IllegalArgumentException("Refund request DENIED!! Refund period is over.");
        }

        p.setRefundReason(refundReason);
        return purchaseRepository.save(p);
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

    public List<Purchase> getClientPurchaseHistory90Days(int clientId) {
        List<Purchase> purchases = getClientPurchaseHistory(clientId);
        // checking if purchases == null is useless
        // because it is already checked in the full history
        List<Purchase> filteredPurchases = purchases.stream()
                .filter(purchase -> purchase.getPurchaseDate().toLocalDate().isAfter(LocalDate.now().minusDays(90)))
                .collect(Collectors.toList());
        return filteredPurchases;
    }
}