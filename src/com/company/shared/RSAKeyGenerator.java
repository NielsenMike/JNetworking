package com.company.shared;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class RSAKeyGenerator {

    public static KeyPair generateKey(int keyLength){
        KeyPair pair = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keyLength);
            pair = keyGen.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return  pair;
    }

    public static PublicKey validatePublicKey(byte[] keyBytes) throws GeneralSecurityException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
        System.out.println("Connected");
        return publicKey;
    }

}
