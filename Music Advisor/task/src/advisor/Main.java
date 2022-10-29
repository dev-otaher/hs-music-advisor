package advisor;

public class Main {
    public static void main(String[] args) {
        if (args.length > 2 && "-access".equals(args[0])) {
            Config.AUTH_SERVER = args[1];
        }
        if (args.length > 3 && "-resource".equals(args[2])) {
            Config.API_SERVER = args[3];
        }
        MusicAdvisor advisor = new MusicAdvisor();
        advisor.start();
    }
}