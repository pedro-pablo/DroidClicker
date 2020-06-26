package com.pedropablo.trabalhofinal;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static byte[] gerarHash(String texto) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(texto.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean compararHash(String texto, byte[] hash) {
        byte[] textoHash = gerarHash(texto);

        if (textoHash == null) {
            return false;
        }

        for (int i = 0; i < hash.length; i++) {
            if (textoHash[i] != hash[i]) {
                return false;
            }
        }

        return true;
    }

}
