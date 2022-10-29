package advisor;

import java.net.URI;
import java.net.http.HttpRequest;

public class GetRequest {
    public static HttpRequest create(String endpoint, String accessToken) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(endpoint))
                .GET()
                .build();
    }
}
