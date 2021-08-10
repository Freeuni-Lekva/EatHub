package ge.eathub.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.eathub.utils.ObjectMapperFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

public class TokenGenerator {
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SECRET_KEY = Encoders.BASE64.encode(key.getEncoded());
    public static final ObjectMapper mapper = ObjectMapperFactory.get();
    public static final SecureRandom rand = new SecureRandom();

    public static String getRandomToken(String Username) {
        long nowMillis = System.currentTimeMillis();
        String keySource = (Username + nowMillis) + rand.nextLong();
        return Encoders.BASE64.encode(keySource.getBytes());
    }

    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signingKey, signatureAlgorithm);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.serializeToJsonWith(new JacksonSerializer(mapper)).compact();
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parserBuilder().deserializeJsonWith(new JacksonDeserializer<>(mapper))
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build()
                .parseClaimsJws(jwt).getBody();
    }


}
