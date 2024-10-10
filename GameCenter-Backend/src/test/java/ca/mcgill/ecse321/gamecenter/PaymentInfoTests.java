package ca.mcgill.ecse321.gamecenter;

import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;
import ca.mcgill.ecse321.gamecenter.repository.PaymentInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentInfoTests {

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        paymentInfoRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadPaymentInfo() {
        // Use setters to create PaymentInfo instance
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardNumber("1234567890123456");
        paymentInfo.setCvv(123);
        paymentInfo.setExpiryMonth(12);
        paymentInfo.setExpiryYear(2025);

        // Persist the PaymentInfo object
        paymentInfo = paymentInfoRepository.save(paymentInfo);
        assertNotNull(paymentInfo);
        assertEquals("1234567890123456", paymentInfo.getCardNumber());
        assertEquals(123, paymentInfo.getCvv());
        assertEquals(12, paymentInfo.getExpiryMonth());
        assertEquals(2025, paymentInfo.getExpiryYear());

        // Get by ID tests
        int id = paymentInfo.getId();
        PaymentInfo paymentInfoFromDb = paymentInfoRepository.findById(id).orElse(null);
        assertNotNull(paymentInfoFromDb);

        assertEquals(id, paymentInfoFromDb.getId());
        assertEquals("1234567890123456", paymentInfoFromDb.getCardNumber());
        assertEquals(123, paymentInfoFromDb.getCvv());
        assertEquals(12, paymentInfoFromDb.getExpiryMonth());
        assertEquals(2025, paymentInfoFromDb.getExpiryYear());
    }

    @Test
    public void testUpdatePaymentInfo() {
        // Create and save PaymentInfo instance using setters
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardNumber("1234567890123456");
        paymentInfo.setCvv(123);
        paymentInfo.setExpiryMonth(12);
        paymentInfo.setExpiryYear(2025);
        paymentInfo = paymentInfoRepository.save(paymentInfo);

        // Update the PaymentInfo attributes using setters
        paymentInfo.setCardNumber("6543210987654321");
        paymentInfo.setCvv(321);
        paymentInfo.setExpiryMonth(6);
        paymentInfo.setExpiryYear(2024);

        // Save the updated PaymentInfo object
        paymentInfo = paymentInfoRepository.save(paymentInfo);
        assertNotNull(paymentInfo);
        assertEquals("6543210987654321", paymentInfo.getCardNumber());
        assertEquals(321, paymentInfo.getCvv());
        assertEquals(6, paymentInfo.getExpiryMonth());
        assertEquals(2024, paymentInfo.getExpiryYear());

        // Retrieve the updated PaymentInfo by ID
        PaymentInfo paymentInfoFromDb = paymentInfoRepository.findById(paymentInfo.getId()).orElse(null);
        assertNotNull(paymentInfoFromDb);

        assertEquals("6543210987654321", paymentInfoFromDb.getCardNumber());
        assertEquals(321, paymentInfoFromDb.getCvv());
        assertEquals(6, paymentInfoFromDb.getExpiryMonth());
        assertEquals(2024, paymentInfoFromDb.getExpiryYear());
    }

    @Test
    public void testDeletePaymentInfo() {
        // Create and save PaymentInfo instance using setters
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardNumber("1234567890123456");
        paymentInfo.setCvv(123);
        paymentInfo.setExpiryMonth(12);
        paymentInfo.setExpiryYear(2025);
        paymentInfo = paymentInfoRepository.save(paymentInfo);
        assertNotNull(paymentInfo);

        // Delete the PaymentInfo
        paymentInfoRepository.deleteById(paymentInfo.getId());

        // Verify it's deleted
        PaymentInfo deletedPaymentInfo = paymentInfoRepository.findById(paymentInfo.getId()).orElse(null);
        assertNull(deletedPaymentInfo);
    }

    @Test
    public void testGetPaymentInfoByCardNumber() {
        // Create and save PaymentInfo instance using setters
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardNumber("1234567890123456");
        paymentInfo.setCvv(123);
        paymentInfo.setExpiryMonth(12);
        paymentInfo.setExpiryYear(2025);
        paymentInfo = paymentInfoRepository.save(paymentInfo);

        // Retrieve by card number using repository method
        PaymentInfo paymentInfoFromDb = paymentInfoRepository.findByCardNumber("1234567890123456").orElse(null);
        assertNotNull(paymentInfoFromDb);
        assertEquals(paymentInfo.getId(), paymentInfoFromDb.getId());
        assertEquals(paymentInfo.getCardNumber(), paymentInfoFromDb.getCardNumber());
    }

    @Test
    public void testGetPaymentInfoByExpiryMonth() {
        // Create and save PaymentInfo instance using setters
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardNumber("1234567890123456");
        paymentInfo.setCvv(123);
        paymentInfo.setExpiryMonth(12);
        paymentInfo.setExpiryYear(2025);
        paymentInfo = paymentInfoRepository.save(paymentInfo);

        // Retrieve by expiry month using repository method
        PaymentInfo paymentInfoFromDb = paymentInfoRepository.findByExpiryMonth(12).orElse(null);
        assertNotNull(paymentInfoFromDb);
        assertEquals(paymentInfo.getId(), paymentInfoFromDb.getId());
        assertEquals(paymentInfo.getExpiryMonth(), paymentInfoFromDb.getExpiryMonth());
    }
}
