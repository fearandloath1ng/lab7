package com.daniel.client.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public final class MD5Password {

    private MD5Password() {
    }
    public static String encryptPassword(String password) {
        String crypted = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            crypted = format(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return crypted;
    }

    private static String format(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte bytee : bytes) {
            formatter.format("%02x", bytee);
        }
        String res = formatter.toString();
        formatter.close();
        return res;
    }
}
