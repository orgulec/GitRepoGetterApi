package org.example.gitrepogetterapi.controller;

import lombok.SneakyThrows;
import org.example.gitrepogetterapi.api.HttpClientService;
import org.example.gitrepogetterapi.api.dto.DtoMapper;
import org.example.gitrepogetterapi.api.dto.GitBranchDto;
import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.NoSuchRepositoriesException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.exceptions.WrongUserNameException;
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

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitReposServiceTest {

    @InjectMocks
    private GitReposService gitReposService;
    @Mock
    private HttpClientService<GitRepo[]> mockRepoClientService;// = new HttpClientService<>();
    @Mock
    private HttpClientService<RepoBranch[]> mockBranchClientService;
    @Mock
    private DtoMapper mapper;
    @Test
    void getReposAndBranchesFromApiByUser_shouldReturnListOfGitReposDto() {
        //given
        String userName = "testUser";
        String repoName = "testRepo";
        String branchName = "testBranch";
        String commitSha = "testCommit_sha";

        GitRepo mockRepo = new GitRepo(repoName, new RepoOwner(userName), false);
        RepoBranch mockBranch = new RepoBranch(branchName, new BranchCommit(commitSha));
        mockBranch.setRepoName(repoName);

        GitRepo[] mockRepositoriesList = {mockRepo};
        RepoBranch[] mockBranchList = {mockBranch};

        GitReposDto expectedDto = new GitReposDto(repoName, userName);
        expectedDto.setBranches(List.of(new GitBranchDto(branchName, commitSha)));
        List<GitReposDto> expectedDtoList = List.of(expectedDto);

        //when
        when(mockRepoClientService.makeHttpRequestGetHttpResponse(userName,GitRepo[].class)).thenReturn(mockRepositoriesList);
        when(mockBranchClientService.makeHttpRequestGetHttpResponse(userName,RepoBranch[].class)).thenReturn(mockBranchList);
        when(mapper.mapReposAndBranchesIntoDto(any(),any())).thenReturn(expectedDtoList);

        //then
        List<GitReposDto> result = gitReposService.getReposAndBranchesFromApiByUser(userName);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedDtoList.size(), result.size()),
                () -> assertEquals(expectedDtoList.getFirst().getName(), result.getFirst().getName()),
                () -> assertEquals(expectedDtoList.getFirst().getOwner(), result.getFirst().getOwner()),
                () -> assertEquals(expectedDtoList.getFirst().getBranches().size(), result.getFirst().getBranches().size())
        );
    }

    @Test
    void getReposAndBranchesFromApiByUser_shouldThrowNoSuchRepositoriesExceptionWhenUserHasNoRepos() {
        //given
        String userName = "testUser";
        //when
//        when(mockRepoClientService.makeHttpRequestGetHttpResponse(userName,GitRepo[].class)).thenReturn(new GitRepo[]{});

        //then
        assertThrows(NoSuchRepositoriesException.class, () -> gitReposService.getReposAndBranchesFromApiByUser(userName));
    }
    @SneakyThrows
    @Test
    void getReposAndBranchesFromApiByUser_shouldThrowUserNotFoundExceptionWhenUserDoesntExists() {
        //given
        String userName = "testUser";
        //when
//        when(apiService.getRepositories(userName)).thenThrow(UserNotFoundException.class);
//        when(mockRepoClientService.makeHttpRequestGetHttpResponse(any(),any())).thenThrow(UserNotFoundException.class);
        when(mockRepoClientService.makeHttpRequestGetHttpResponse(userName,GitRepo[].class)).thenThrow(UserNotFoundException.class);

        //then
        assertThrows(UserNotFoundException.class, () -> gitReposService.getReposAndBranchesFromApiByUser(userName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "", " wrong user ", "/ o", "*", "$user"})
    void getReposAndBranchesFromApiByUser_shouldThrowWrongUserNameExceptionByEmptyUsername(String userName) {
        //given

        //when//then
        assertThrows(WrongUserNameException.class, () -> gitReposService.getReposAndBranchesFromApiByUser(userName));
    }
}