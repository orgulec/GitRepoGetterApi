package org.example.gitrepogetterapi.api;

import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RestApiService {
    private final RestClient restClient;
    private final String BASE_URL = "https://api.github.com";

    public RestApiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Gets repositories data from api url by specific username
     *
     * @param userName the specific username of the gitHub user.
     * @return list of GitRepo with data from Json.
     */
    public List<GitRepo> getRepositoriesByUsername(String userName) {
        return restClient.get()
                .uri("/users/" + userName + "/repos")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * Gets branches data from api url by specific username and repository name
     *
     * @param userName the specific username of the gitHub user.
     * @param repoName the specific repository name belonging to the user.
     * @return list of RepoBranch with data from Json.
     */
    public List<RepoBranch> getBranchesByUsernameAndRepository(String userName, String repoName) {
        return restClient.get()
                .uri("/repos/" + userName + "/" + repoName + "/branches")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
