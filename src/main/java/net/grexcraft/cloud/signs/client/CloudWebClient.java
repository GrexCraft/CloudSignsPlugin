package net.grexcraft.cloud.signs.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.grexcraft.cloud.signs.dto.ImageDto;
import net.grexcraft.cloud.signs.dto.PoolSlotDto;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class CloudWebClient {

    private static final String apiUrl = "http://cloud-service:8080";
    static ObjectMapper objectMapper = new ObjectMapper();

    public static PoolSlotDto getPoolSlotByName(String name) {
        try {
            String response = getPoolSlotByNameRequest(name);
            PoolSlotDto slot = objectMapper.readValue(response, PoolSlotDto.class);

            return slot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageDto getImageForPool(String pool) {
        try {
            String response = getImageForPoolRequest(pool);
            ImageDto image = objectMapper.readValue(response, ImageDto.class);

            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getPoolSlotByNameRequest(String name) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/v1/slot/name/" + name + "/"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private static String getImageForPoolRequest(String pool) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/v1/image/pool/" + pool + "/"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static void createServerRequest(ImageDto image) {
        Bukkit.getLogger().info("Sending create request to cloud service");

        var values = new HashMap<String, String>() {{
            put("image", image.getName());
            put("tag", image.getTag());
        }};

        var objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper
                    .writeValueAsString(values);
            Bukkit.getLogger().info("object mapper wrote values");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("requestBody: " + requestBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/v1/server/create"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


//    private static final WebClient client =WebClient.create(apiUrl);
//
//    public static PoolSlotDto getPoolSlotByName(String name) {
//        Bukkit.getLogger().info("get pool by name " + name);
//        return client.get()
//                .uri("/api/v1/slot/name/" + name + "/")
//                .retrieve()
//                .bodyToMono(PoolSlotDto.class).block();
//    }
}
