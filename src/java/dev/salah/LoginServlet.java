package dev.salah;

import dev.salah.beans.User;
import dev.salah.beans.Cart;
import dev.salah.services.CartWS;
import dev.salah.services.CategoryWS;
import dev.salah.services.UserWS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

//            // Cart Logic
//            List<Cart> userCarts = CartWS.readByUserId(user.getId());
//            List<Cart> carts = (List<Cart>) request.getSession().getAttribute("carts");
//            if (carts == null) {
//                carts = userCarts;
//            } else {
//                for (Cart cart : userCarts) {
//                    if (!carts.contains(cart)) {
//                        carts.add(cart);
//                    }
//                }
//            }
//            request.getSession().setAttribute("carts", carts);
//            CartWS.create(carts);
//            // Cart Logic END
            // Cart Logic
            List<Cart> userCarts = CartWS.readByUserId(user.getId());
            List<String> cart = (List<String>) request.getSession().getAttribute("cart");
            if (cart == null) {
                cart = userCarts.stream().map(c -> c.getBookId().getId().toString()).collect(Collectors.toList());
            } else {
                for (Cart userCart : userCarts) {
                    final String id = userCart.getBookId().getId().toString();
                    if (!cart.contains(id)) {
                        cart.add(id);
                    }
                }
            }
            request.getSession().setAttribute("cart", cart);
            CartWS.create(user, cart);
            // Cart Logic END

            response.sendRedirect(request.getServletContext().getContextPath());
        } else {
            response.sendRedirect(request.getServletContext().getContextPath() + "/Home/Login.jsp");
        }
    }
}
