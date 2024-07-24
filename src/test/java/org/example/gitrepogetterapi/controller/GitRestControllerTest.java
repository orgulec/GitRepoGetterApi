package org.example.gitrepogetterapi.controller;

import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.InvalidUserNameException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class GitRestControllerTest {

    @InjectMocks
    private GitRestController gitRestController;
    @Mock
    private GitRestService gitRestService;

    @Test
    void getByUserName_shouldReturnGitReposDtoWithStatus200() {
        //given
        String userName = "testUser";
        GitReposDto mockReposDto = new GitReposDto("testRepo", userName);
        List<GitReposDto> mockListOfGitReposDto = new ArrayList<>();
        mockListOfGitReposDto.add(mockReposDto);
        String header = "application/json";
        //when
        when(gitRestService.getByUserName(userName)).thenReturn(mockListOfGitReposDto);
        //then
        ResponseEntity<List<GitReposDto>> result = gitRestController.getByUserName(header, userName);
        assertAll(
                () -> assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> assertEquals(ResponseEntity.ok(mockListOfGitReposDto), result),
                () -> assertNotNull(result.getBody()),
                () -> assertEquals(mockListOfGitReposDto.size(), result.getBody().size())
        );
    }

    @Test
    void getByUserName_shouldReturnUserNotFoundException() {
        //given
        String userName = "testUser";
        String header = "application/json";
        //when
        when(gitRestService.getByUserName(userName)).thenThrow(new UserNotFoundException(userName));
        //then
        assertThrows(UserNotFoundException.class, () -> gitRestController.getByUserName(header, userName));
    }

    @Test
    void getByUserName_shouldReturnWrongUserNameException() {
        //given
        String userName = "test User";
        String header = "application/json";
        //when
        when(gitRestService.getByUserName(userName)).thenThrow(new InvalidUserNameException());
        //then
        assertThrows(InvalidUserNameException.class, () -> gitRestController.getByUserName(header, userName));
    }

    @Test
    void getWithNoUser_shouldReturnUserNotFoundException() {
        //given
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> gitRestController.getWithNoUser());
    }
}