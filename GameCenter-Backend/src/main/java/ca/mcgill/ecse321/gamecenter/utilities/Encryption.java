package ca.mcgill.ecse321.gamecenter.utilities;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Encryption {
    private static final char key = '?';
   public static String encryptDecrypt(String password) {
       return password.chars()
               .map(c -> c ^ key)
               .mapToObj(c -> String.valueOf((char) c))
               .collect(Collectors.joining());
   }
}
