package advisor;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] featuredPlaylists;
    static String[] newAlbums;
    static String[] availableCategories;
    static String[] playlistOfCategory;

    static {
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
    }

    public static void main(String[] args) {
        final String clientId = "f5efa89948ad4d3fba609bb84ffc9716";
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean authenticated = false;
        do {
            input = scanner.nextLine().toLowerCase();
            if ("auth".equals(input)) {
                authenticated = true;
            }
            if (authenticated) {
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
                    case "auth":
                        printAuthLink(clientId);
                        break;
                    default:
                        break;
                }
                if (input.startsWith("playlists ")) {
                    String category = input.split(" ")[1];
                    printPlaylistOfCategory(category);
                }
            } else {
                System.out.println("Please, provide access for application.");
            }
        } while (!"exit".equals(input));
        System.out.println("---GOODBYE!---");
    }

    private static void printAuthLink(String clientId) {
        System.out.printf("https://accounts.spotify.com/authorize" +
                          "?client_id=%s" +
                          "&redirect_uri=http://localhost:8080&response_type=code\n"
                , clientId
        );
        System.out.println("---SUCCESS---");
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
