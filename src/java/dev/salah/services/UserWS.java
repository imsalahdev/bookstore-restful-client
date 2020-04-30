package dev.salah.services;

import dev.salah.beans.User;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.BinaryDataStrategy;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class UserWS {

    private static UsersFacadeREST_JerseyClient client = new UsersFacadeREST_JerseyClient();
    private static JsonbConfig config = new JsonbConfig()
            .withBinaryDataStrategy(BinaryDataStrategy.BASE_64);
    private static Jsonb builder = JsonbBuilder.create(config);

    public static void create(User user) {
        client.create(builder.toJson(user));
    }

    public static List<User> read() {
        return builder.fromJson(client.findAll(String.class), new ArrayList<User>() {
        }.getClass().getGenericSuperclass());
    }

    public static User read(String id) {
        return builder.fromJson(client.find(String.class, id), User.class);
    }

    public static User readByEmail(String email) {
        System.out.println(email);
        return builder.fromJson(client.findByEmail(String.class, email), User.class);
    }

    public static void update(User user) {
        client.edit(builder.toJson(user), String.valueOf(user.getId()));
    }

    public static void delete(String id) {
        client.remove(id);
    }

    static class UsersFacadeREST_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8080/bookstore-restful-ws/webresources";

        public UsersFacadeREST_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("dev.salah.users");
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

        public <T> T findByEmail(Class<T> responseType, String email) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("email/{0}", new Object[]{email}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
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
