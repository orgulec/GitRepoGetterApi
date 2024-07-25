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

    /**
     * Gets repositories data from api url by specific username
     *
     * @param userName the specific username of the gitHub user.
     * @return array of GitRepo with data from Json.
     */
    public GitRepo[] getRepositoriesByUsername(String userName) {
        return restClient.get()
                .uri("/users/" + userName + "/repos")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ParameterizedTypeReference.forType(GitRepo[].class));
    }

    /**
     * Gets branches data from api url by specific username and repository name
     *
     * @param userName the specific username of the gitHub user.
     * @param repoName the specific repository name belonging to the user.
     * @return array of RepoBranch with data from Json.
     */
    public RepoBranch[] getBranchesByUsernameAndRepository(String userName, String repoName) {
        return restClient.get()
                .uri("/repos/" + userName + "/" + repoName + "/branches")
                .retrieve()
                .body(ParameterizedTypeReference.forType(RepoBranch[].class));
    }
}
