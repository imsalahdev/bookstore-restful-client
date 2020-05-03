package dev.salah;

import dev.salah.ws.User;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            return null;
        }
    }

    public static void markAsPublic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            response.sendRedirect(request.getServletContext().getContextPath());
        }
    }

    public static void markAsPrivate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        markAsPrivate(request, response, false);
    }

    public static void markAsPrivate(HttpServletRequest request, HttpServletResponse response, Boolean needsAdminPrivilege) throws IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null || (user.getRole().equals("user") && needsAdminPrivilege)) {
            response.sendRedirect(request.getServletContext().getContextPath() + "/Home/Error.jsp");
        }
    }
}
