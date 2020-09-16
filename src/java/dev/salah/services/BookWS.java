package dev.salah.services;

import com.google.gson.Gson;
import dev.salah.ws.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class BookWS {

    private static final BooksFacadeREST_JerseyClient client = new BooksFacadeREST_JerseyClient();
    private static final Gson builder = new Gson();

    public static void create(Book book) {
        client.create(builder.toJson(book));
    }

    public static List<Book> read() {
        return builder.fromJson(client.findAll(String.class), new ArrayList<Book>() {}.getClass().getGenericSuperclass());
    }

    public static Book read(String id) {
        return builder.fromJson(client.find(String.class, id), Book.class);
    }

    public static List<Book> read(List<String> ids) {
        List<Book> books = new ArrayList<>();
        ids.forEach(id -> {
            books.add(read(id));
        });
        return books;
    }

    public static List<Book> readByCategoryID(Integer id) {
        List<Book> books = read();
        books.removeIf(book -> !Objects.equals(book.getCategoryID().getId(), id));
        return books;
    }

    public static String count() {
        return client.countREST();
    }

    public static void update(Book book) {
        client.edit(builder.toJson(book), String.valueOf(book.getId()));
    }

    public static void delete(String id) {
        client.remove(id);
    }

    static class BooksFacadeREST_JerseyClient {

        private final WebTarget webTarget;
        private final Client client;
        private static final String BASE_URI = "http://localhost:8080/bookstore-restful-ws/webresources";

        public BooksFacadeREST_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("dev.salah.books");
        }

        public String countREST() throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path("count");
            return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
        }

        public void edit(Object requestEntity, String id) throws ClientErrorException {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        }

        public <T> T find(Class<T> responseType, String id) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public <T> T findRange(Class<T> responseType, String from, String to) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public void create(Object requestEntity) throws ClientErrorException {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        }

        public <T> T findAll(Class<T> responseType) throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public void remove(String id) throws ClientErrorException {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
        }

        public void close() {
            client.close();
        }
    }
}
