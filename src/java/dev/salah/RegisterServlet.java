package dev.salah;

import dev.salah.services.UserWS;
import dev.salah.ws.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet(name = "RegisterServlet", urlPatterns = {"/Register"})
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(Utils.hashPassword(request.getParameter("password")));
        user.setRole("user");
        user.setPhoto(Utils.resizeImage(request.getPart("photo").getInputStream()));
        UserWS.create(user);
        response.sendRedirect(request.getServletContext().getContextPath() + "/Home/Login.jsp");
    }
}
