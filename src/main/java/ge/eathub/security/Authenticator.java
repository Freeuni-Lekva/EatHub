package ge.eathub.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Authenticator {

    private final SecureRandom random;

    private final Cache<String, String> accessTokens;

    private static final Map<String, String> users = new ConcurrentHashMap<>();
    public static final int TOKEN_EXPIRE_TIME_IN_SECONDS = 15;

    public Authenticator() {
        users.put("asd", "a");
        users.put("foo", "a");
        users.put("bar", "a");
        users.put("tra", "a");
        random = new SecureRandom();
        accessTokens = CacheBuilder.newBuilder()
                .expireAfterAccess(TOKEN_EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }


    public boolean checkCredentials(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public String issueAccessToken(String username) {
        String accessToken = generateRandomString();
        accessTokens.put(accessToken, username);
        return accessToken;
    }

    public Optional<String> getUsernameFromToken(String accessToken) {
        String username = accessTokens.getIfPresent(accessToken);
        if (username == null) {
            return Optional.empty();
        } else {
            accessTokens.invalidate(accessToken);
            return Optional.of(username);
        }
    }

    private String generateRandomString() {
        return new BigInteger(130, random).toString(32);
    }
}
