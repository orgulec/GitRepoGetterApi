package org.example.gitrepogetterapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.gitrepogetterapi.api.HttpClientService;
import org.example.gitrepogetterapi.api.dto.DtoMapper;
import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.NoSuchRepositoriesException;
import org.example.gitrepogetterapi.api.exceptions.WrongUserNameException;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
class GitReposService {

    private final String BASE_GIT_URL = "https://api.github.com/";
    private final HttpClientService<GitRepo[]> repoClientService = new HttpClientService<>();
    private final HttpClientService<RepoBranch[]> branchClientService = new HttpClientService<>();
    private final DtoMapper mapper = new DtoMapper();

    List<GitReposDto> getReposAndBranchesFromApiByUser(String userName) {
        if (userName.isEmpty() || !userName.matches("[a-zA-Z0-9-_]+")) {
            throw new WrongUserNameException();
        }
        String url = BASE_GIT_URL + "users/" + userName + "/repos";

        List<GitRepo> repositories = Arrays.stream(
                repoClientService.makeHttpRequestGetHttpResponse(url, GitRepo[].class))
                .toList();

        List<List<RepoBranch>> branchesData = new ArrayList<>();
        repositories.forEach(repo ->
                branchesData.add(getBranchesToRepositories(userName, repo.name())));

        return mapper.mapReposAndBranchesIntoDto(repositories, branchesData);
    }

    List<RepoBranch> getBranchesToRepositories(String userName, String repoName) {
        String url = BASE_GIT_URL + "repos/" + userName + "/" + repoName + "/branches";

        List<RepoBranch> branchList = Arrays.stream(
                branchClientService.makeHttpRequestGetHttpResponse(url, RepoBranch[].class))
                .toList();
        branchList.forEach(br -> br.setRepoName(repoName));
        return branchList;
    }
}
