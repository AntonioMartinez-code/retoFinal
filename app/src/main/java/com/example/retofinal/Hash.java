package com.example.retofinal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String Hashear(String contrasena) throws NoSuchAlgorithmException {

        String paraHash = contrasena;
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte dataBytes[] = paraHash.getBytes();
        md.update(dataBytes);
        byte resumen[] = md.digest();
        String hash = new String(resumen);

        return hash;
    }
}
