package ca.mcgill.ecse321.gamecenter.service;

import ca.mcgill.ecse321.gamecenter.model.Client;
import ca.mcgill.ecse321.gamecenter.model.PaymentInfo;
import ca.mcgill.ecse321.gamecenter.repository.AppUserRepository;
import ca.mcgill.ecse321.gamecenter.repository.PaymentInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        Client c = new Client(email, username, password, phoneNumber, deliveryAddress, 0);
        c.setId(21);
        when(appUserRepository.save(any(Client.class))).thenReturn(c);
        when(appUserRepository.findAppUserById(c.getId())).thenReturn(Optional.of(c));
        Client createdClient = appUserService.createClientAccount(email, username, password, phoneNumber, deliveryAddress);

        String cardNumber = "12830142";
        Integer cvv = 111;
        Integer expiryMonth = 12;
        Integer expiryYear = 2029;
        PaymentInfo savedPaymentInfo = paymentInfoService.savePaymentInfo(
            cardNumber, cvv, expiryMonth, expiryYear, createdClient
        );

        assertEquals(savedPaymentInfo.getClient().getId(), createdClient.getId());
    }
}
