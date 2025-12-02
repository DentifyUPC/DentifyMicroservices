package com.upc.dentify.patientattentionservice.infrastructure.persistence.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptoService {
    private static final String ALGO = "AES";
    private static final String TRANSFORM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int IV_LENGTH = 12; // bytes


    private final SecretKey key;
    private final SecureRandom rnd = new SecureRandom();


    private static CryptoService INSTANCE;


    private CryptoService(byte[] keyBytes) {
        this.key = new SecretKeySpec(keyBytes, ALGO);
    }


    public static synchronized void initWithKeyBytes(byte[] keyBytes) {
        if (INSTANCE == null) INSTANCE = new CryptoService(keyBytes);
    }


    public static CryptoService getInstance() {
        if (INSTANCE == null) throw new IllegalStateException("CryptoService not initialized");
        return INSTANCE;
    }


    public String encryptBase64(String plain) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            rnd.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] cipherText = cipher.doFinal(plain == null ? new byte[0] : plain.getBytes(StandardCharsets.UTF_8));


            ByteBuffer bb = ByteBuffer.allocate(iv.length + cipherText.length);
            bb.put(iv);
            bb.put(cipherText);
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (Exception e) {
            throw new RuntimeException("encryption failed", e);
        }
    }


    public String decryptBase64(String base64) {
        try {
            byte[] all = Base64.getDecoder().decode(base64);
            if (all.length < IV_LENGTH) throw new IllegalArgumentException("ciphertext too short");
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(all, 0, iv, 0, IV_LENGTH);
            byte[] cipherText = new byte[all.length - IV_LENGTH];
            System.arraycopy(all, IV_LENGTH, cipherText, 0, cipherText.length);


            Cipher cipher = Cipher.getInstance(TRANSFORM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("decryption failed", e);
        }
    }
}