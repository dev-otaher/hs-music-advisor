package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class SpotifyAuthorizer {
    private String authorizationCode;
    private String accessToken;
    private final Server server;
    private final HttpClient httpClient;
    private boolean authorized;

    {
        authorizationCode = "";
        accessToken = "";
        server = new Server(Config.SOCKET_ADDRESS);
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        authorized = false;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void authorize() {
        server.start();
        printAuthorizationUrl();
        System.out.println("waiting for code...");
        while ("".equals(server.getAuthorizationCode())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("code received...");
        authorized = true;
        authorizationCode = server.getAuthorizationCode();
        server.stop(1);
        System.out.println("making http request for access_token...");
        HttpRequest request = prepareAccessTokenRequest();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
            accessToken = responseBody.get("access_token").getAsString();
            System.out.println("Success!");
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printAuthorizationUrl() {
        String url = String.format(
                "%s/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                Config.AUTH_SERVER,
                Config.CLIENT_ID,
                Config.REDIRECT_URL);
        System.out.println("use this link to request the access code");
        System.out.println(url);
    }

    private HttpRequest prepareAccessTokenRequest() {
        String body = String.format(
                "grant_type=%s&redirect_uri=%s&code=%s",
                "authorization_code",
                Config.REDIRECT_URL,
                authorizationCode);
        String clientIdClientSecret = String.format("%s:%s", Config.CLIENT_ID, Config.CLIENT_SECRET);
        String clientIdClientSecretBase64 = Base64.getEncoder().encodeToString(clientIdClientSecret.getBytes());
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", String.format("Basic %s", clientIdClientSecretBase64))
                .uri(URI.create(Config.AUTH_SERVER + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}
