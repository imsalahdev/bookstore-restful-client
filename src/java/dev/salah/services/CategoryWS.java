package dev.salah.services;

import dev.salah.ws.Category;
import jakarta.json.bind.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class CategoryWS {

    private static CategoriesFacadeREST_JerseyClient client = new CategoriesFacadeREST_JerseyClient();
    private static Jsonb builder = JsonbBuilder.create();

    public static void create(Category category) {
        client.create(builder.toJson(category));
    }

    public static List<Category> read() {
        return builder.fromJson(client.findAll(String.class), new ArrayList<Category>() {
        }.getClass().getGenericSuperclass());
    }

    public static Category read(String id) {
        return builder.fromJson(client.find(String.class, id), Category.class);
    }

    public static Category readByName(String name) {
        return builder.fromJson(client.findByName(String.class, name), Category.class);
    }

    public static void update(Category category) {
        client.edit(builder.toJson(category), String.valueOf(category.getId()));
    }

    public static void delete(String id) {
        client.remove(id);
    }

    static class CategoriesFacadeREST_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8080/bookstore-restful-ws/webresources";

        public CategoriesFacadeREST_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("dev.salah.categories");
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

        public <T> T findByName(Class<T> responseType, String name) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("name/{0}", new Object[]{name}));
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
