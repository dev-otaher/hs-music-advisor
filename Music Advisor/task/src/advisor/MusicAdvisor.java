package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Scanner;

public class MusicAdvisor {
    private final SpotifyClient spotifyClient;

    {
        spotifyClient = new SpotifyClient();
    }

    private void printPlaylists(JsonArray playlists) {
        playlists.forEach(playlist -> {
            String name = playlist.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
            String link = playlist.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .getAsJsonPrimitive("spotify")
                    .getAsString();
            System.out.println(name);
            System.out.println(link + "\n");
        });
    }

    private void printCategories() {
        spotifyClient.requestCategories().forEach(category ->
                System.out.println(category.getAsJsonObject().getAsJsonPrimitive("name").getAsString())
        );
    }

    private void printFeaturedPlaylists() {
        JsonArray featuredPlaylists = spotifyClient.requestFeaturedPlaylists();
        printPlaylists(featuredPlaylists);
    }

    public void printNewReleases() {
        JsonArray albums = spotifyClient.requestNewReleases();
        for (JsonElement album : albums) {
            String name = album.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
            String link = album.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .getAsJsonPrimitive("spotify")
                    .getAsString();
            JsonArray artists = album.getAsJsonObject().getAsJsonArray("artists");
            System.out.println(name);
            StringBuilder artistsString = new StringBuilder("[");
            artists.forEach(artist -> artistsString.append(artist.getAsJsonObject().get("name").getAsString()).append(", "));
            artistsString.append("]");
            System.out.println(artistsString.toString().replace(", ]", "]"));
            System.out.println(link + "\n");
        }
    }

    private void printPlaylistsOfCategory(String categoryName) {
        String categoryId = spotifyClient.getCategoryIdByName(categoryName);
        if ("".equals(categoryId)) {
            System.out.println("Unknown category name.");
            return;
        }
        JsonObject response = spotifyClient.requestPlaylistOfCategory(categoryId);
        if (response.has("error")) {
            System.out.println(
                    response.getAsJsonObject("error")
                            .getAsJsonPrimitive("message").getAsString()
            );
            return;
        }
        JsonArray playlists = response.getAsJsonObject("playlists").getAsJsonArray("items");
        printPlaylists(playlists);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            input = scanner.nextLine().toLowerCase();
            if (spotifyClient.isAuthorized()) {
                switch (input) {
                    case "featured":
                        printFeaturedPlaylists();
                        break;
                    case "new":
                        printNewReleases();
                        break;
                    case "categories":
                        printCategories();
                        break;
                    default:
                        break;
                }
                if (input.startsWith("playlists ")) {
                    String categoryName = input.replace("playlists ", "");
                    printPlaylistsOfCategory(categoryName);
                }
            } else if ("auth".equals(input)) {
                spotifyClient.authorize();
            } else if (!"exit".equals(input)) {
                System.out.println("Please, provide access for application.");
            }
        } while (!"exit".equals(input));
    }
}
