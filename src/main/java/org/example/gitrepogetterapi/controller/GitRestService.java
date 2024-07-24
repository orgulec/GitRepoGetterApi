package org.example.gitrepogetterapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.gitrepogetterapi.api.RestApiService;
import org.example.gitrepogetterapi.api.dto.DtoMapper;
import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.InvalidUserNameException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GitRestService {

    private final RestApiService restApiService;
    private final DtoMapper mapper = new DtoMapper();

    public List<GitReposDto> getByUserName(String userName) {
        if (userName.isEmpty() || !userName.matches("[a-zA-Z0-9-_]+")) {
            throw new InvalidUserNameException();
        }
        List<GitRepo> repositories = getRepositoriesByUserName(userName);

        List<List<RepoBranch>> branchesData = new ArrayList<>();
        repositories.stream()
                .filter(repo -> !repo.fork())
                .forEach(repo ->
                        branchesData.add(getBranchesToRepo(userName, repo.name())));

        return mapper.mapReposAndBranchesIntoDto(repositories, branchesData);
    }

    List<GitRepo> getRepositoriesByUserName(String userName) {
        try {
            GitRepo[] repositories = restApiService.getRepositoriesByUsername(userName);
            return Arrays.stream(repositories).toList();
        } catch (HttpClientErrorException e) {
            throw new UserNotFoundException(userName);
        }
    }

    List<RepoBranch> getBranchesToRepo(String userName, String repoName) {
        List<RepoBranch> branchList = Arrays.stream(restApiService.getBranchesByUsernameAndRepository(userName, repoName)).toList();
        branchList.forEach(branch -> branch.setRepoName(repoName));
        return branchList;
    }
}
