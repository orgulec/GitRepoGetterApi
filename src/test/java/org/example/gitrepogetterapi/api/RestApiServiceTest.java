package org.example.gitrepogetterapi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gitrepogetterapi.api.git_model.BranchCommit;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.example.gitrepogetterapi.api.git_model.RepoOwner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(RestApiService.class)
class RestApiServiceTest {

    @Autowired
    MockRestServiceServer server;
    @Autowired
    RestApiService restApiService;
    @Autowired
    ObjectMapper objectMapper;

    private final String BASE_URL = "https://api.github.com";

    @Test
    void getRepositoriesByUsername_shouldGetDataByUserName() throws JsonProcessingException {
        //given
        String userName = "userTest";
        GitRepo[] data = {
                new GitRepo("repo1", new RepoOwner(userName), false),
                new GitRepo("repo2", new RepoOwner(userName), false)};
        //when
        server.expect(requestTo(BASE_URL + "/users/" + userName + "/repos"))
                .andRespond(withSuccess(objectMapper.writeValueAsBytes(data), MediaType.APPLICATION_JSON));
        //then
        List<GitRepo> result = restApiService.getRepositoriesByUsername(userName);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(data.length, result.size())
        );
    }

    @Test
    void getBranchesByUsernameAndRepository_shouldGetDataByUserNameAndRepoName() throws JsonProcessingException {
        //given
        String userName = "userTest";
        String repoName = "repoTest";
        RepoBranch[] data = {
                new RepoBranch("branch1", new BranchCommit("test_sha1")),
                new RepoBranch("branch2", new BranchCommit("test_sha2"))};
        //when
        server.expect(requestTo(BASE_URL + "/repos/" + userName + "/" + repoName + "/branches"))
                .andRespond(withSuccess(objectMapper.writeValueAsBytes(data), MediaType.APPLICATION_JSON));
        //then
        List<RepoBranch> result = restApiService.getBranchesByUsernameAndRepository(userName, repoName);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(data.length, result.size())
        );
    }
}