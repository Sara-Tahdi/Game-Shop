package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.PaymentInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentInfoServiceTests {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    @InjectMocks
    private AppUserService appUserService;
    @InjectMocks
    private PaymentInfoService paymentInfoService;

    @Test
    public void testAddUserPaymentInfo() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        String cvv = "111";
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, createdClient);
        when(paymentInfoRepository.save(any(PaymentInfo.class))).thenReturn(paymentInfo);

        PaymentInfo savedPaymentInfo = paymentInfoService.savePaymentInfo(
            cardNumber, cvv, expiryMonth, expiryYear, createdClient
        );
        assertNotNull(savedPaymentInfo);
        assertEquals(savedPaymentInfo.getClient().getId(), createdClient.getId());
        assertEquals(savedPaymentInfo.getCardNumber(), cardNumber);
        assertEquals(savedPaymentInfo.getCvv(), cvv);
        assertEquals(savedPaymentInfo.getExpiryMonth(), expiryMonth);
        assertEquals(savedPaymentInfo.getExpiryYear(), expiryYear);
    }

    @Test
    public void testAddDuplicatePaymentInfo() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        String cvv = "111";
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, createdClient);
        when(paymentInfoRepository.save(any(PaymentInfo.class))).thenReturn(paymentInfo);
        when(paymentInfoRepository.findPaymentInfoByCardNumber(cardNumber))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(paymentInfo));

        PaymentInfo savedPaymentInfo = paymentInfoService.savePaymentInfo(
                cardNumber, cvv, expiryMonth, expiryYear, createdClient
        );
        assertNotNull(savedPaymentInfo);
        assertEquals(savedPaymentInfo.getClient().getId(), createdClient.getId());

        IllegalArgumentException e =
            assertThrows(IllegalArgumentException.class, () -> paymentInfoService.savePaymentInfo(
                cardNumber, cvv, expiryMonth, expiryYear, createdClient
            ));

        assertEquals(e.getMessage(), "A payment info with card number "+cardNumber+" already exists in the system.");
    }

    @Test
    public void testDeleteExistingPaymentInfo() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        String cvv = "111";
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, createdClient);
        when(paymentInfoRepository.save(any(PaymentInfo.class))).thenReturn(paymentInfo);

        PaymentInfo savedPaymentInfo = paymentInfoService.savePaymentInfo(
                cardNumber, cvv, expiryMonth, expiryYear, createdClient
        );
        assertNotNull(savedPaymentInfo);
        assertEquals(savedPaymentInfo.getClient().getId(), createdClient.getId());

        when(paymentInfoRepository.findPaymentInfoById(paymentInfo.getId())).thenReturn(Optional.of(paymentInfo));

        paymentInfoService.deletePaymentInfo(paymentInfo.getId());

        when(paymentInfoRepository.findPaymentInfoById(paymentInfo.getId())).thenReturn(Optional.empty());
        PaymentInfo deletedPaymentInfo = paymentInfoRepository.findPaymentInfoById(paymentInfo.getId()).orElse(null);
        assertNull(deletedPaymentInfo);
    }

    @Test
    public void testDeleteNonexistingPaymentInfo() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        String cvv = "111";
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, createdClient);
        when(paymentInfoRepository.save(any(PaymentInfo.class))).thenReturn(paymentInfo);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
          paymentInfoService.deletePaymentInfo(paymentInfo.getId());
        });
        assertEquals(e.getMessage(), "No payment info with id: " + paymentInfo.getId());
    }

    @Test
    public void testFindExistingPaymentInfo() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        String cvv = "111";
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, createdClient);
        when(paymentInfoRepository.save(any(PaymentInfo.class))).thenReturn(paymentInfo);

        PaymentInfo savedPaymentInfo = paymentInfoService.savePaymentInfo(
                cardNumber, cvv, expiryMonth, expiryYear, createdClient
        );
        assertNotNull(savedPaymentInfo);
        assertEquals(savedPaymentInfo.getClient().getId(), createdClient.getId());

        when(paymentInfoRepository.findPaymentInfoById(paymentInfo.getId())).thenReturn(Optional.of(paymentInfo));
        PaymentInfo foundPaymentInfo = paymentInfoService.findPaymentInfoById(paymentInfo.getId());
        assertNotNull(foundPaymentInfo);
        assertEquals(foundPaymentInfo.getClient().getId(), createdClient.getId());
    }

    @Test
    public void testFindNonexistingPaymentInfo() {
        String email = "user1@gma.ca";
        String username = "Dave";
        String password = "VeryRich";
        String phoneNumber = "5141234567";
        String deliveryAddress = "123 John Street";
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        String cvv = "111";
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryMonth, expiryYear, createdClient);
        when(paymentInfoRepository.save(any(PaymentInfo.class))).thenReturn(paymentInfo);


        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            paymentInfoService.findPaymentInfoById(paymentInfo.getId());
        });
        assertEquals(e.getMessage(), "No payment info with id: " + paymentInfo.getId());
    }
}
