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
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            input = scanner.nextLine().toLowerCase();
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
                case "exit":
                    System.out.println("---GOODBYE!---");
                    break;
                default:
                    break;
            }
            if (input.startsWith("playlists ")) {
                String category = input.split(" ")[1];
                printPlaylistOfCategory(category);
            }
        } while (!"exit".equals(input));
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
