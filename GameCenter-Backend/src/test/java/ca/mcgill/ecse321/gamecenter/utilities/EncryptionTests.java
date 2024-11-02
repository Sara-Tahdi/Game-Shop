package ca.mcgill.ecse321.gamecenter.utilities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class EncryptionTests {
    @Test
    public void testEncrypt() {
        String password = "IBringTheBoom";
        String encrypted = Encryption.encryptDecrypt(password);

        assertNotEquals(password, encrypted);
    }

    @Test
    public void testEncryptionReversed() {
        String password = "431bfc28g1b3bwad-QBF0qwfdF";
        String doubleEncrypted = Encryption.encryptDecrypt(Encryption.encryptDecrypt(password));

        assertEquals(password, doubleEncrypted);
    }
}
