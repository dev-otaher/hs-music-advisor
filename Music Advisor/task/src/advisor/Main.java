package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class Main {
    static final String clientId;
    static final String clientSecret;
    static final String[] featuredPlaylists;
    static final String[] newAlbums;
    static final String[] availableCategories;
    static final String[] playlistOfCategory;
    static final HttpClient httpClient;
    static String authorizationCode;
    static HttpServer server;
    static boolean authorized;
    static String accessServerPoint;
    static String redirectUri;

    static {
        clientId = "f5efa89948ad4d3fba609bb84ffc9716";
        clientSecret = "031281f99c52440484cb527d657e8042";
        authorizationCode = "";
        featuredPlaylists = new String[]{
                "Mellow Morning",
                "Wake Up and Smell the Coffee",
                "Monday Motivation",
                "Songs to Sing in the Shower"
        };
        newAlbums = new String[]{
                "Mountains [Sia, Diplo, Labrinth]",
                "Runaway [Lil Peep]",
                "The Greatest Show [Panic! At The Disco]",
                "All Out Life [Slipknot]"
        };
        availableCategories = new String[]{
                "Top Lists",
                "Pop",
                "Mood",
                "Latin"
        };
        playlistOfCategory = new String[]{
                "Walk Like A Badass",
                "Rage Beats",
                "Arab Mood Booster",
                "Sunday Stroll"
        };
        prepareServer();
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        authorized = false;
        redirectUri = "http://localhost:8090";
    }

    public static void main(String[] args) {
        accessServerPoint = (args.length == 2 && "-access".equals(args[0])) ? args[1] : "https://accounts.spotify.com";
        Scanner scanner = new Scanner(System.in);
        String input;
        server.start();
        do {
            input = scanner.nextLine().toLowerCase();
            if (authorized) {
                switch (input) {
                    case "featured":
                        printFeaturedPlaylists();
                        break;
                    case "new":
                        printNewAlbums();
                        break;
                    case "categories":
                        printAvailableCategories();
                        break;
                    default:
                        break;
                }
                if (input.startsWith("playlists ")) {
                    String category = input.replace("playlists ", "");
                    printPlaylistOfCategory(category);
                }
            } else if ("auth".equals(input)) {
                authorize();
            } else if (!"exit".equals(input)) {
                System.out.println("Please, provide access for application.");
            }
        } while (!"exit".equals(input));
        server.stop(1);
        System.out.println("---GOODBYE!---");
    }

    private static void prepareServer() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8090), 0);
            server.createContext("/",
                    httpExchange -> {
                        String query = httpExchange.getRequestURI().getQuery();
                        System.out.println(query);
                        String msg;
                        if (query == null || !query.contains("code=")) {
                            msg = "Authorization code not found. Try again.";
                        } else {
                            System.out.println("code received...");
                            authorizationCode = query.replace("code=", "");
                            msg = "Got the code. Return back to your program.";
                        }
                        httpExchange.sendResponseHeaders(200, msg.length());
                        httpExchange.getResponseBody().write(msg.getBytes());
                        httpExchange.getResponseBody().close();
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void authorize() {
        printAuthenticationUrl();
        System.out.println("waiting for code...");
        while ("".equals(authorizationCode)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        server.stop(1);
        System.out.println("making http request for access_token...");
        HttpRequest request = prepareAccessTokenRequest();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response:\n" + response.body());
            authorized = true;
            System.out.println("---SUCCESS---");
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest prepareAccessTokenRequest() {
        String body = String.format(
                "grant_type=%s&redirect_uri=%s&code=%s",
                "authorization_code",
                redirectUri,
                authorizationCode);
        String clientIdClientSecret = String.format("%s:%s", clientId, clientSecret);
        String clientIdClientSecretBase64 = Base64.getEncoder().encodeToString(clientIdClientSecret.getBytes());
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", String.format("Basic %s", clientIdClientSecretBase64))
                .uri(URI.create(accessServerPoint + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private static void printAuthenticationUrl() {
        String url = String.format(
                "%s/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                accessServerPoint,
                clientId,
                redirectUri);
        System.out.println("use this link to request the access code");
        System.out.println(url);
    }

    private static void printPlaylistOfCategory(String category) {
        System.out.printf("---%s PLAYLISTS---\n", category.toUpperCase());
        Arrays.stream(playlistOfCategory)
                .forEach(System.out::println);
    }

    private static void printAvailableCategories() {
        System.out.println("---CATEGORIES---");
        Arrays.stream(availableCategories)
                .forEach(System.out::println);
    }

    private static void printNewAlbums() {
        System.out.println("---NEW RELEASES---");
        Arrays.stream(newAlbums)
                .forEach(System.out::println);
    }

    private static void printFeaturedPlaylists() {
        System.out.println("---FEATURED---");
        Arrays.stream(featuredPlaylists)
                .forEach(System.out::println);
    }
}
