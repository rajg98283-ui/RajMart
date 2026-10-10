package com.raj.rajmart.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for password hashing and verification using bcrypt (jBCrypt).
 */
public final class PasswordUtil {

    private PasswordUtil() {
        // utility class
    }

    /**
     * Hashes a plaintext password using bcrypt.
     *
     * @param plaintext the raw password
     * @return bcrypt hash
     */
    public static String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(10));
    }

    /**
     * Verifies a plaintext password against a stored bcrypt hash.
     *
     * @param plaintext the raw password
     * @param hash      the stored hash
     * @return true if match
     */
    public static boolean verify(String plaintext, String hash) {
        if (plaintext == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(plaintext, hash);
    }
}
