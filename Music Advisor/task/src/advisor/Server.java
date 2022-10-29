package advisor;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private int socketAddress;
    private HttpServer server;
    private String authorizationCode;

    {
        authorizationCode = "";
    }

    public Server(int socketAddress) {
        this.socketAddress = socketAddress;
    }

    private void create() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(socketAddress), 0);
            server.createContext("/", httpExchange -> {
                String query = httpExchange.getRequestURI().getQuery();
                String msg;
                if (query == null || !query.contains("code=")) {
                    msg = "Authorization code not found. Try again.";
                } else {
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

    public void start() {
        create();
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
