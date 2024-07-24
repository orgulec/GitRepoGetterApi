package org.example.gitrepogetterapi.api;

import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RestApiService {
    private final RestClient restClient;

    public RestApiService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.github.com")
                .build();
    }

    public GitRepo[] getRepositoriesByUsername(String userName) {
        return restClient.get()
                .uri("/users/" + userName + "/repos")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ParameterizedTypeReference.forType(GitRepo[].class));
    }

    public RepoBranch[] getBranchesByUsernameAndRepository(String userName, String repoName) {
        return restClient.get()
                .uri("/repos/" + userName + "/" + repoName + "/branches")
                .retrieve()
                .body(ParameterizedTypeReference.forType(RepoBranch[].class));
    }
}
