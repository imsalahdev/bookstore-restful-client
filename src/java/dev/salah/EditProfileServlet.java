package dev.salah;

import dev.salah.beans.User;
import dev.salah.services.UserWS;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet(name = "EditProfileServlet", urlPatterns = {"/EditProfile"})
public class EditProfileServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        
        String prevPassword = user.getPassword();
        String newPassword = request.getParameter("password");
        if (!prevPassword.equals(newPassword)) {
            user.setPassword(Utils.hashPassword(newPassword));
        }
        
        InputStream inputStream = request.getPart("photo").getInputStream();
        byte[] photoBytes = new byte[inputStream.available()];
        inputStream.read(photoBytes);
        if (photoBytes.length != 0) {
            user.setPhoto(photoBytes);
        }
        UserWS.update(user);
        
        request.getSession().setAttribute("user", user);
        response.sendRedirect(request.getServletContext().getContextPath());
    }
}
