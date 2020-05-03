package dev.salah.servlets;

import dev.salah.services.CategoryWS;
import dev.salah.ws.Category;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet(name = "CategoriesServlet", urlPatterns = {"/categories"})
public class CategoriesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        String method = request.getParameter("method");
        if (method.equals("POST") || method.equals("PUT")) {
            if (method.equals("POST")) {
                Category category = new Category();
                category.setName(request.getParameter("name"));
                CategoryWS.create(category);
            } else {
                Category category = CategoryWS.read(id);
                category.setName(request.getParameter("name"));

                CategoryWS.update(category);
            }
        } else if (method.equals("DELETE")) {
            CategoryWS.delete(id);
        }
        response.sendRedirect(request.getServletContext().getContextPath() + "/Categories");
    }
}
