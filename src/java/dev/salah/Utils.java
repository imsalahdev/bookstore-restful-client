package dev.salah;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.mindrot.jbcrypt.BCrypt;

public class Utils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(8));
    }

    public static Boolean verifyPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    public static byte[] resizeImage(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(Thumbnails.of(is).size(148, 188).asBufferedImage(), "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
