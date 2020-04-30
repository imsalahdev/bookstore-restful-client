package dev.salah;

import org.mindrot.jbcrypt.BCrypt;

public class Utils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(8));
    }

    public static Boolean verifyPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
