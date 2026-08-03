package org.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtil {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {

        // RSA generator
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        // 2048 bit vali
        generator.initialize(2048);
        // Public + Private key generate karo
        return generator.generateKeyPair();

    }
}