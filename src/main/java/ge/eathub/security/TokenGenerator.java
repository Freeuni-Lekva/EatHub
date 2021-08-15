package ge.eathub.security;

import io.jsonwebtoken.io.Encoders;

import java.security.SecureRandom;

public class TokenGenerator {
    public static final SecureRandom rand = new SecureRandom();

    public static String getRandomToken(String Username) {
        long nowMillis = System.currentTimeMillis();
        String keySource = (Username + nowMillis) + rand.nextLong();
        return Encoders.BASE64.encode(keySource.getBytes());
    }

}
