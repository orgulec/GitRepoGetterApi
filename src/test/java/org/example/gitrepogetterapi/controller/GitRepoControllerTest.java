package org.example.gitrepogetterapi.controller;

import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.NoSuchRepositoriesException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.exceptions.WrongUserNameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitRepoControllerTest {

    @Mock
    private GitReposService gitReposService;
    @InjectMocks
    private GitRepoController gitRepoController;

    @Test
    void getGitRepoByUser_shouldReturnListOfReposDtoWithStatus200() {
        //given
        String userName = "testUser";
        GitReposDto mockReposDto = new GitReposDto("testRepo", userName);
        List<GitReposDto> mockListOfGitReposDto = new ArrayList<>();
        mockListOfGitReposDto.add(mockReposDto);
        String header = "application/json";

        //when
        when(gitReposService.getReposAndBranchesFromApiByUser(userName)).thenReturn(mockListOfGitReposDto);

        //then
        ResponseEntity<List<GitReposDto>> result = gitRepoController.getGitRepoByUser(header, userName);
        assertAll(
                () -> assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> assertEquals(ResponseEntity.ok(mockListOfGitReposDto), result),
                () -> assertNotNull(result.getBody()),
                () -> assertEquals(mockListOfGitReposDto.size(), result.getBody().size())
        );
    }

    @Test
    void getGitRepoByUser_shouldReturnUserNotFoundException() {
        //given
        String userName = "testUser";
        String header = "application/json";
        //when
        when(gitReposService.getReposAndBranchesFromApiByUser(userName)).thenThrow(new UserNotFoundException(userName));
        //then
        assertThrows(UserNotFoundException.class, () -> gitRepoController.getGitRepoByUser(header, userName));
    }

    @Test
    void getGitRepoByUser_shouldReturnNoSuchRepositoriesException() {
        //given
        String userName = "testUser";
        String header = "application/json";
        //when
        when(gitReposService.getReposAndBranchesFromApiByUser(userName)).thenThrow(new NoSuchRepositoriesException(userName));
        //then
        assertThrows(NoSuchRepositoriesException.class, () -> gitRepoController.getGitRepoByUser(header, userName));
    }

    @Test
    void getGitRepoByUser_shouldReturnWrongUserNameException() {
        //given
        String userName = "testUser";
        String header = "application/json";
        //when
        when(gitReposService.getReposAndBranchesFromApiByUser(userName)).thenThrow(new WrongUserNameException());
        //then
        assertThrows(WrongUserNameException.class, () -> gitRepoController.getGitRepoByUser(header, userName));
    }
}