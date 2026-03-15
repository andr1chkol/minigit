package minigit.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    public static String sha1(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(data);
            StringBuilder sb = new StringBuilder(40);
            for(byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash the file", e);
        }

    }
}
