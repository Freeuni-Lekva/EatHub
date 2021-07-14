package ge.eathub.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Authenticator {

    private final Cache<String, String> accessTokens;
    public static final int TOKEN_EXPIRE_TIME_IN_SECONDS = 15;

    public Authenticator() {
        accessTokens = CacheBuilder.newBuilder()
                .expireAfterWrite(TOKEN_EXPIRE_TIME_IN_SECONDS, TimeUnit.MINUTES)
                .expireAfterAccess(TOKEN_EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    public String getAccessToken(String username) {
        String accessToken = TokenGenerator.getRandomToken(username);
        accessTokens.put(accessToken, username);
        return accessToken;
    }

    public Optional<String> getUsername(String accessToken) {
        String username = accessTokens.getIfPresent(accessToken);
        if (username == null) {
            return Optional.empty();
        } else {
            accessTokens.invalidate(accessToken);
            return Optional.of(username);
        }
    }

}
