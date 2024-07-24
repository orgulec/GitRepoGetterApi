package org.example.gitrepogetterapi.api;

import lombok.SneakyThrows;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {
//    @InjectMocks
//    @Spy
//    private ApiService apiService;
    @Mock
    private HttpClient client;
    @Mock
    private HttpRequest mockRequest;
    @Mock
    private HttpResponse<String> mockResponse;


    @SneakyThrows
    @Test
    void getRepositories_shouldReturnListOfGitRepo() {
        //given
        String url = "https://api.github.com/";
        String userName = "testUser";
        String mockJson = "[{\"name\":\"testRepo\",\"owner\":{\"login\":\"testUser\"},\"fork\":false}]";


        //when
        when(mockResponse.body()).thenReturn(mockJson);
        when(mockResponse.statusCode()).thenReturn(200);
        when(client.send(mockRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockResponse);
//        when(spy(httpApiService.makeHttpRequestGetHttpResponse(url))).thenReturn(mockResponse);


        //then
//        List<GitRepo> result = apiService.getRepositories(userName);
//        assertAll(
//                ()->assertNotNull(result),
//                ()->assertEquals(1, result.size())
//        );
    }

    @Test
    void getBranches() {
        //given

        //when

        //then

    }
}