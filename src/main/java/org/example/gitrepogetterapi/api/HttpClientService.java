package org.example.gitrepogetterapi.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.example.gitrepogetterapi.api.exceptions.NoSuchRepositoriesException;
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

        HttpResponse<String> httpResponse;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new WrongUserNameException();
        }
        return returnObjectFromJson(responseClass, httpResponse);
    }

    T returnObjectFromJson(Class<T> responseClass, HttpResponse<String> httpResponse) {
        if (httpResponse.statusCode() != 200) {
            throw new UserNotFoundException();
        }
        Gson gson = new Gson();
        try {
            return gson.fromJson(httpResponse.body(), responseClass);
        } catch (JsonSyntaxException e) {
            throw new NoSuchRepositoriesException("No repositories founded.");
        }
    }
}
