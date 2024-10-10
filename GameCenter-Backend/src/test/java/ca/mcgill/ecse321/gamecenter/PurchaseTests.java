package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.Game;
import ca.mcgill.ecse321.gamecenter.model.Purchase;
import ca.mcgill.ecse321.gamecenter.repository.ClientRepository;
import ca.mcgill.ecse321.gamecenter.repository.GameRepository;
import ca.mcgill.ecse321.gamecenter.repository.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PurchaseTests {
    @Autowired
    PurchaseRepository purchaseRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        purchaseRepository.deleteAll();
    }

    @Test
    void testPersistAndLoadPurchase() {
        Purchase purchase = new Purchase();
        purchase = purchaseRepository.save(purchase);
        assertNotNull(purchase);

        Purchase purchaseFromDb = purchaseRepository.findPurchaseById(purchase.getId()).orElse(null);
        assertNotNull(purchaseFromDb);

        assertEquals(purchase.getId(), purchaseFromDb.getId());
    }
}
