package dev.salah.servlets;

import dev.salah.beans.Book;
import dev.salah.beans.Category;
import dev.salah.services.BookWS;
import dev.salah.services.CategoryWS;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet(name = "BooksServlet", urlPatterns = {"/books"})
public class BooksServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String method = request.getParameter("method");
        if (method.equals("POST") || method.equals("PUT")) {
            if (method.equals("POST")) {
                Book book = new Book();
                book.setTitle(request.getParameter("title"));
                book.setAuthor(request.getParameter("author"));
                book.setInformation(request.getParameter("information"));

                book.setCategoryID(CategoryWS.read(request.getParameter("categoryID")));

                book.setPrice(Double.valueOf(request.getParameter("price")));

                InputStream inputStream = request.getPart("thumbnail").getInputStream();
                byte[] thumbnailBytes = new byte[inputStream.available()];
                inputStream.read(thumbnailBytes);
                book.setThumbnail(thumbnailBytes);

                book.setCount(Integer.valueOf(request.getParameter("count")));

                BookWS.create(book);
            } else {
                Book book = BookWS.read(id);
                book.setTitle(request.getParameter("title"));
                book.setAuthor(request.getParameter("author"));
                book.setInformation(request.getParameter("information"));

                book.setCategoryID(CategoryWS.read(request.getParameter("categoryID")));

                book.setPrice(Double.valueOf(request.getParameter("price")));

                InputStream inputStream = request.getPart("thumbnail").getInputStream();
                byte[] thumbnailBytes = new byte[inputStream.available()];
                inputStream.read(thumbnailBytes);
                if (thumbnailBytes.length != 0) {
                    book.setThumbnail(thumbnailBytes);
                }

                book.setCount(Integer.valueOf(request.getParameter("count")));

                BookWS.update(book);
            }
        } else if (method.equals("DELETE")) {
            BookWS.delete(id);
        }
        response.sendRedirect(request.getServletContext().getContextPath() + "/Books");
    }
}
