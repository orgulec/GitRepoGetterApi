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

    private final DtoMapper mapper = new DtoMapper();
    private final String BASE_GIT_URL = "https://api.github.com/";
    private final HttpClientService<GitRepo[]> repoClientService = new HttpClientService<>();
    private final HttpClientService<RepoBranch[]> branchClientService = new HttpClientService<>();

    List<GitReposDto> getReposAndBranchesFromApiByUser(String userName) {
        if (userName.isEmpty() || !userName.matches("[a-zA-Z0-9-_]+")) {
            throw new WrongUserNameException();
        }

        List<GitRepo> repositories = getRepositories(userName);
        List<List<RepoBranch>> branchesData = new ArrayList<>();

        if (repositories.isEmpty()) {
            throw new NoSuchRepositoriesException(userName);
        } else {
            repositories.forEach(repo ->
                    branchesData.add(getBranchesToRepositories(userName, repo.name())));
        }
        return mapper.mapReposAndBranchesIntoDto(repositories, branchesData);
    }

    List<GitRepo> getRepositories(String userName) {
        String url = BASE_GIT_URL + "users/" + userName + "/repos";

        GitRepo[] gitRepos = repoClientService.makeHttpRequestGetHttpResponse(url, GitRepo[].class);
        return Arrays.stream(gitRepos).toList();
    }

    List<RepoBranch> getBranchesToRepositories(String userName, String repoName) {
        String url = BASE_GIT_URL + "repos/" + userName + "/" + repoName + "/branches";

        RepoBranch[] repoBranches = branchClientService.makeHttpRequestGetHttpResponse(url, RepoBranch[].class);
        List<RepoBranch> branchList = Arrays.stream(repoBranches).toList();
        branchList.forEach(br -> br.setRepoName(repoName));
        return branchList;
    }
}
