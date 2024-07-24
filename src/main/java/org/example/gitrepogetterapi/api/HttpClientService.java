package org.example.gitrepogetterapi.api;

import com.google.gson.Gson;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.exceptions.WrongUserNameException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class HttpClientService<T> {
    private final HttpClient client = HttpClient.newHttpClient();

    public T makeHttpRequestGetHttpResponse(String url, Class<T> responseClass) throws UserNotFoundException, WrongUserNameException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new UserNotFoundException();
            }
            Gson gson = new Gson();
            return gson.fromJson(response.body(), responseClass);
        } catch (IOException | InterruptedException e) {
            throw new WrongUserNameException();
        }
    }
}
