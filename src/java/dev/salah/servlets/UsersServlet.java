package dev.salah.servlets;

import dev.salah.Utils;
import dev.salah.beans.User;
import dev.salah.services.UserWS;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet(name = "UsersServlet", urlPatterns = {"/users"})
public class UsersServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        
        String method = request.getParameter("method");
        if (method.equals("POST") || method.equals("PUT")) {
            if (method.equals("POST")) {
                User user = new User();
                user.setUsername(request.getParameter("username"));
                user.setEmail(request.getParameter("email"));
                user.setPassword(Utils.hashPassword(request.getParameter("password")));
                user.setIsAdmin(request.getParameterValues("isAdmin") != null);
                
                InputStream inputStream = request.getPart("photo").getInputStream();
                byte[] photoBytes = new byte[inputStream.available()];
                inputStream.read(photoBytes);
                
                user.setPhoto(photoBytes);
                UserWS.create(user);
            } else {
                User user = UserWS.read(id);
                String prevPassword = user.getPassword();
                String newPassword = request.getParameter("password");
                user.setUsername(request.getParameter("username"));
                user.setEmail(request.getParameter("email"));
                if (!prevPassword.equals(newPassword)) {
                    user.setPassword(Utils.hashPassword(newPassword));
                }
                user.setIsAdmin(request.getParameterValues("isAdmin") != null);
                
                InputStream inputStream = request.getPart("photo").getInputStream();
                byte[] photoBytes = new byte[inputStream.available()];
                inputStream.read(photoBytes);
                if (photoBytes.length != 0) {
                    user.setPhoto(photoBytes);
                }
                
                UserWS.update(user);
            }
        } else if (method.equals("DELETE")) {
            UserWS.delete(id);
        }
        response.sendRedirect(request.getServletContext().getContextPath() + "/Users");
    }
}
