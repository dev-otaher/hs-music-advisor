package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyClient {
    private final HttpClient httpClient;
    private final SpotifyAuthorizer authorizer;

    {
        httpClient = HttpClient.newBuilder().build();
        authorizer = new SpotifyAuthorizer();
    }

    private HttpRequest createRequest(String endpoint) {
        return GetRequest.create(endpoint, authorizer.getAccessToken());
    }

    private JsonObject request(String endpoint) {
        try {
            HttpRequest categoriesRequest = createRequest(endpoint);
            HttpResponse<String> response = httpClient.send(categoriesRequest, HttpResponse.BodyHandlers.ofString());
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonArray requestCategories() {
        String endpoint = Config.API_SERVER + Config.CATEGORIES_ENDPOINT;
        return request(endpoint)
                .getAsJsonObject("categories")
                .getAsJsonArray("items");
    }

    public JsonArray requestFeaturedPlaylists() {
        String endpoint = Config.API_SERVER + Config.FEATURED_PLAYLISTS_ENDPOINT;
        return request(endpoint)
                .getAsJsonObject("playlists")
                .getAsJsonArray("items");
    }

    public JsonArray requestNewReleases() {
        String endpoint = Config.API_SERVER + Config.NEW_RELEASES_ENDPOINT;
        return request(endpoint)
                .get("albums")
                .getAsJsonObject()
                .get("items")
                .getAsJsonArray();
    }

    public JsonObject requestPlaylistOfCategory(String categoryId) {
        String endpoint = Config.API_SERVER + Config.CATEGORIES_ENDPOINT + Config.PLAYLISTS_OF_CATEGORY_ENDPOINT;
        return request(String.format(endpoint, categoryId));
    }

    public String getCategoryIdByName(String categoryName) {
        for (JsonElement category : requestCategories()) {
            String name = category.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
            if (name.equalsIgnoreCase(categoryName)) {
                return category.getAsJsonObject().getAsJsonPrimitive("id").getAsString();
            }
        }
        return "";
    }

    public boolean isAuthorized() {
        return authorizer.isAuthorized();
    }

    public void authorize() {
        authorizer.authorize();
    }

}
