package com.library.literatura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIFetcher {

    public static String getData(String url) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println("HTTP Status Code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching data from URL: " + url, e);
        }

        return response.body();
    }
}
