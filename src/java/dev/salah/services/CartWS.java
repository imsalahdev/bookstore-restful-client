package dev.salah.services;

import com.google.gson.Gson;
import dev.salah.ws.Cart;
import dev.salah.ws.User;
import javax.json.bind.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class CartWS {

    private static final CartsFacadeREST_JerseyClient client = new CartsFacadeREST_JerseyClient();
    private static final Gson builder = new Gson();

    public static void create(Cart cart) {
        if (readByCart(cart.getUserId().getId(), cart.getBookId().getId()) == null) {
            System.out.println("add");
            client.create(builder.toJson(cart));
        }
    }

    public static void create(User user, List<String> bookIDs) {
        for (String id : bookIDs) {
            final Cart cart = new Cart();
            cart.setUserId(user);
            cart.setBookId(BookWS.read(id));
            create(cart);
        }
    }

    public static Cart read(String id) {
        return builder.fromJson(client.find(String.class, id), Cart.class);
    }

    public static Cart readByCart(Integer uid, Integer bid) {
        try {
            return builder.fromJson(client.findByCart(String.class, uid.toString(), bid.toString()), Cart.class);
        } catch (JsonbException e) {
            return null;
        }

    }

    public static void removeByCart(Integer uid, Integer bid) {
        client.removeByCart(uid.toString(), bid.toString());
    }

    public static List<Cart> readByUserId(Integer userId) {
        return builder.fromJson(client.findByUserId(String.class, userId.toString()), new ArrayList<Cart>() {
        }.getClass().getGenericSuperclass());
    }

    public static void removeByUserID(Integer uid) {
        client.removeByUserID(String.valueOf(uid));
    }

    static class CartsFacadeREST_JerseyClient {

        private final WebTarget webTarget;
        private final Client client;
        private static final String BASE_URI = "http://localhost:8080/bookstore-restful-ws/webresources";

        public CartsFacadeREST_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("dev.salah.carts");
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

        public <T> T findByUserId(Class<T> responseType, String userId) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("userId/{0}", new Object[]{userId}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public void create(Object requestEntity) throws ClientErrorException {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        }

        public void removeByUserID(String userId) throws ClientErrorException {
            webTarget.path(java.text.MessageFormat.format("cart/{0}", new Object[]{userId})).request().delete();
        }

        public void removeByCart(String userId, String bookId) throws ClientErrorException {
            webTarget.path(java.text.MessageFormat.format("cart/{0}/{1}", new Object[]{userId, bookId})).request().delete();
        }

        public <T> T findAll(Class<T> responseType) throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public <T> T findByCart(Class<T> responseType, String userId, String bookId) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("cart/{0}/{1}", new Object[]{userId, bookId}));
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
