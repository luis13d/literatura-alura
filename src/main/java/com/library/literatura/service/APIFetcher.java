package com.library.literatura.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class APIFetcher {

    private final HttpClient httpClient;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    public APIFetcher() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public String getData(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        try {
            URI uri = new URI(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(REQUEST_TIMEOUT)
                    .header("User-Agent", "Literatura-App/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            validateResponse(response, url);
            return response.body();
            
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("Network error while fetching data from: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request was interrupted while fetching data from: " + url, e);
        }
    }

    private void validateResponse(HttpResponse<String> response, String url) {
        int statusCode = response.statusCode();
        
        if (statusCode >= 200 && statusCode < 300) {
            return; // Success
        }
        
        String errorMessage = switch (statusCode) {
            case 400 -> "Bad request to API";
            case 401 -> "Unauthorized access to API";
            case 403 -> "Forbidden access to API";
            case 404 -> "API endpoint not found";
            case 429 -> "Rate limit exceeded";
            case 500 -> "Internal server error at API";
            case 503 -> "API service unavailable";
            default -> "HTTP error " + statusCode;
        };
        
        throw new RuntimeException(errorMessage + " (URL: " + url + ")");
    }
}
