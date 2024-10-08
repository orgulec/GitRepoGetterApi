package org.example.gitrepogetterapi.controller;

import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.InvalidUserNameException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitRestController.class)
class GitRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GitRestService gitRestService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getByUserName_shouldReturnGitReposDtoList() throws Exception {
        //given
        String userName = "testUser";
        String name = "repoName";
        GitReposDto gitDto = new GitReposDto(name, userName, new ArrayList<>());
        List<GitReposDto> gitDtoList = List.of(gitDto);

        String jsonResponse = """
                [
                       {
                            "name": "repoName",
                            "owner": "testUser",
                            "branches": []
                       }
                ]
                """;

        //when
        when(gitRestService.getByUserName(userName)).thenReturn(gitDtoList);
        //then
        mockMvc.perform(get("/" + userName)
                        .header("Accept", "application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    void getByUserName_shouldThrowExceptionWhenUserNameIsEmpty() throws Exception {
        //given
        String userName = "";
        //when
        when(gitRestService.getByUserName(userName)).thenThrow(UserNotFoundException.class);
        //then
        mockMvc.perform(get("/" + userName)
                        .header("Accept", "application/json")
                )
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "*", "$user"})
    void getByUserName_shouldThrowExceptionWhenUserNameIsInvalid(String userName) throws Exception {
        //given
        //when
        when(gitRestService.getByUserName(userName)).thenThrow(InvalidUserNameException.class);
        //then
        mockMvc.perform(get("/" + userName)
                        .header("Accept", "application/json")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByUserName_shouldThrowExceptionWhenUserIsNotFound() throws Exception {
        //given
        String userName = "testUser";

        //when
        when(gitRestService.getByUserName(userName)).thenThrow(UserNotFoundException.class);
        //then
        mockMvc.perform(get("/" + userName)
                        .header("Accept", "application/json")
                )
                .andExpect(status().isNotFound());
    }

}