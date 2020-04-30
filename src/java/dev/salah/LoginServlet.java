package dev.salah;

import dev.salah.beans.User;
import dev.salah.services.UserWS;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = UserWS.readByEmail(email);
        if (user != null && Utils.verifyPassword(password, user.getPassword())) {
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getServletContext().getContextPath());
        } else {
            response.sendRedirect(request.getServletContext().getContextPath() + "/Home/Login.jsp");
        }
    }
}
