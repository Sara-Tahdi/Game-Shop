package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.Promotion;
import ca.mcgill.ecse321.gamecenter.repository.PromotionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PromotionTests {
    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    void testLoadAndPersistPromotion() {
        Promotion promotion = new Promotion();
        promotion.setNewPrice(23.99F);
        Date startDate = new Date(1728659221);
        Date endDate = new Date(1728659221 + 1000 * 60 * 60 * 24);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion = promotionRepository.save(promotion);
        assertNotNull(promotion);

        Promotion promotionFromDb = promotionRepository.findPromotionById(promotion.getId()).orElse(null);
        assertNotNull(promotionFromDb);

        assertEquals(promotion.getId(), promotionFromDb.getId());
        assertEquals(promotion.getNewPrice(), promotionFromDb.getNewPrice());
        assertEquals(promotion.getStartDate().toLocalDate(), promotionFromDb.getStartDate().toLocalDate());
        assertEquals(promotion.getEndDate().toLocalDate(), promotionFromDb.getEndDate().toLocalDate());
    }
}
