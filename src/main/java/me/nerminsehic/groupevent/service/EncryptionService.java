package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.exception.IllegalAccessTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    private final SecretKey key;
    private final IvParameterSpec iv;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private SecretKey generateKey(String password, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.US_ASCII), 65536, 256);

            return new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage()); // should not be reached
        }
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public EncryptionService(@Value("${groupevent.encryption-secret}") String secret,
                             @Value("${groupevent.encryption-salt}") String salt) {
        this.key = generateKey(secret, salt);
        this.iv = generateIv();
    }

    public String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.US_ASCII));
            return Base64.getEncoder()
                    .encodeToString(cipherText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalAccessTokenException("Could not encrypt input");
        }

    }

    public String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
            return new String(plainText, StandardCharsets.US_ASCII);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalAccessTokenException("Could not decrypt input");
        }
    }
}
