package org.example.gitrepogetterapi.controller;

import org.example.gitrepogetterapi.api.RestApiService;
import org.example.gitrepogetterapi.api.dto.GitBranchDto;
import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.InvalidUserNameException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.git_model.BranchCommit;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.example.gitrepogetterapi.api.git_model.RepoOwner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitRestServiceTest {

    @InjectMocks
    private GitRestService gitRestService;
    @Mock
    private RestApiService restApiService;


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "*", "$user"})
    void getByUserName_shouldThrowInvalidUsernameExceptionIfWrongUserName(String userName) {
        //given
        //when
        //then
        assertThrows(InvalidUserNameException.class, () -> gitRestService.getByUserName(userName));
    }

    @Test
    void getByUserName_shouldThrowUserNotFoundExceptionIfUserNotFounded() {
        //given
        String userName = "testUser";
        //when
        when(restApiService.getRepositoriesByUsername(userName)).thenThrow(new UserNotFoundException());
        //then
        assertThrows(UserNotFoundException.class, () -> gitRestService.getByUserName(userName));

    }

    @Test
    void getByUserName_shouldReturnGitReposDto() {
        //given
        String userName = "testUser";
        String repoName = "testRepo";
        String branchName = "testBranch";
        String commitSha = "testCommit_sha";

        GitRepo mockRepo = new GitRepo(repoName, new RepoOwner(userName), false);
        RepoBranch mockBranch = new RepoBranch(branchName, new BranchCommit(commitSha));
        mockBranch.setRepoName(repoName);

        List<GitRepo> mockReposList = List.of(mockRepo);
        List<RepoBranch> mockBranchList = List.of(mockBranch);

        GitReposDto mockRepoDto = new GitReposDto(repoName, userName,List.of(new GitBranchDto(branchName, commitSha)));
        List<GitReposDto> expectedDtoList = List.of(mockRepoDto);
        //when
        when(restApiService.getRepositoriesByUsername(userName)).thenReturn(mockReposList);
        when(restApiService.getBranchesByUsernameAndRepository(userName, repoName)).thenReturn(mockBranchList);
        //then
        List<GitReposDto> result = gitRestService.getByUserName(userName);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedDtoList.size(), result.size()),
                () -> assertEquals(expectedDtoList.getFirst().name(), result.getFirst().name()),
                () -> assertEquals(expectedDtoList.getFirst().owner(), result.getFirst().owner()),
                () -> assertEquals(expectedDtoList.getFirst().branches().size(), result.getFirst().branches().size())
        );
    }

    @Test
    void getRepositoriesByUserName_shouldThrowUserNotFoundException() {
        //given
        String userName = "testUser";
        //when
        when(restApiService.getRepositoriesByUsername(userName)).thenThrow(HttpClientErrorException.class);
        //then
        assertThrows(UserNotFoundException.class, () -> gitRestService.getRepositoriesByUserName(userName));
    }

    @Test
    void getRepositoriesByUserName_shouldReturnGitRepoList() {
        //given
        String userName = "testUser";
        String repoName = "testRepo";
        GitRepo mockRepo = new GitRepo(repoName, new RepoOwner(userName), false);
        List<GitRepo> expectedRepoList = List.of(mockRepo);

        //when
        when(restApiService.getRepositoriesByUsername(userName)).thenReturn(expectedRepoList);
        //then
        List<GitRepo> result = gitRestService.getRepositoriesByUserName(userName);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedRepoList.size(), result.size()),
                () -> assertEquals(expectedRepoList.getFirst().name(), result.getFirst().name()),
                () -> assertEquals(expectedRepoList.getFirst().owner().login(), result.getFirst().owner().login())
        );
    }

    @Test
    void getBranchesToRepo_shouldReturnRepoBranchList() {
        //given
        String userName = "testUser";
        String repoName = "testRepo";
        String branchName = "testBranch";
        String commitSha = "testCommit_sha";
        RepoBranch mockBranch = new RepoBranch(branchName, new BranchCommit(commitSha));
        mockBranch.setRepoName(repoName);

        List<RepoBranch> expectedBranchList = List.of(mockBranch);
        //when
        when(restApiService.getBranchesByUsernameAndRepository(userName, repoName)).thenReturn(expectedBranchList);
        //then
        List<RepoBranch> result = gitRestService.getBranchesToRepo(userName, repoName);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedBranchList.size(), result.size()),
                () -> assertEquals(expectedBranchList.getFirst().getRepoName(), result.getFirst().getRepoName()),
                () -> assertEquals(expectedBranchList.getFirst().getCommit().sha(), result.getFirst().getCommit().sha())
        );
    }
}