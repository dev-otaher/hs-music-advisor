package advisor;

public class Config {
    public static String AUTH_SERVER = "https://accounts.spotify.com";
    public static String API_SERVER = "https://api.spotify.com";
    public static String CATEGORIES_ENDPOINT = "/v1/browse/categories";
    public static String PLAYLISTS_OF_CATEGORY_ENDPOINT = "/%s/playlists";
    public static String NEW_RELEASES_ENDPOINT = "/v1/browse/new-releases";
    public static String FEATURED_PLAYLISTS_ENDPOINT = "/v1/browse/featured-playlists";

    public static String CLIENT_ID = "f5efa89948ad4d3fba609bb84ffc9716";
    public static String CLIENT_SECRET = "";
    public static int SOCKET_ADDRESS = 8080;
    public static String REDIRECT_URL = "http://localhost:" + SOCKET_ADDRESS;
}
