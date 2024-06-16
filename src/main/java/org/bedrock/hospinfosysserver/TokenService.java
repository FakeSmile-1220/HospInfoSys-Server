package org.bedrock.hospinfosysserver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class TokenService {

    private static final String PUBLIC_KEY_FILE = "rsa_public_key.pem";
    private static final String PRIVATE_KEY_FILE = "rsa_private_key.pem";
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public TokenService() {
        KeyPair keyPair = getKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    public String generateToken(String username, String type) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            return JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", username)
                    .withClaim("type", type)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration or Claims can not be converted.
            throw new RuntimeException("Error creating JWT token", exception);
        }
    }

    public boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt != null;
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return false;
        }
    }

    public String getTokenClaim(String token, String claim) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private KeyPair getKeyPair() {
        try {
            File publicKeyFile = new File(PUBLIC_KEY_FILE);
            File privateKeyFile = new File(PRIVATE_KEY_FILE);

            if (publicKeyFile.exists() && privateKeyFile.exists()) {
                // Load keys from files
                PublicKey publicKey = loadPublicKey(publicKeyFile);
                PrivateKey privateKey = loadPrivateKey(privateKeyFile);
                return new KeyPair(publicKey, privateKey);
            } else {
                // Generate and store keys
                KeyPair keyPair = generateKeyPair();
                savePublicKey(keyPair.getPublic(), publicKeyFile);
                savePrivateKey(keyPair.getPrivate(), privateKeyFile);
                return keyPair;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing key pair", e);
        }
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Key size
        return keyPairGenerator.generateKeyPair();
    }

    private void savePublicKey(PublicKey publicKey, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(publicKey.getEncoded());
        }
    }

    private void savePrivateKey(PrivateKey privateKey, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(privateKey.getEncoded());
        }
    }

    private PublicKey loadPublicKey(File file) throws Exception {
        byte[] keyBytes = readAllBytes(file);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private PrivateKey loadPrivateKey(File file) throws Exception {
        byte[] keyBytes = readAllBytes(file);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private byte[] readAllBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
}
