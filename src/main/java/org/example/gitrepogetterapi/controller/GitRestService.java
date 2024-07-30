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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class GitRestService {

    private final RestApiService restApiService;
    private final DtoMapper dtoMapper;

    /**
    * Retrieves all public repositories by username and branches to each of it, matches them by repository name and maps it into list of Dto.
    *
    * @param userName The specific username of the gitHub user.
    * @return list of repositories and their branches mapped into Dto object.
    * @throws InvalidUserNameException if userName is not correct.
    */
    public List<GitReposDto> getByUserName(String userName) {
        if (userName.isEmpty() || !userName.matches("[a-zA-Z0-9-_]+")) {
            throw new InvalidUserNameException();
        }
        List<GitRepo> repositories = getRepositoriesByUserName(userName);

        List<List<RepoBranch>> branchesData = repositories.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> getBranchesToRepo(userName, repo.name()))
                .collect(Collectors.toList());

        return dtoMapper.mapReposAndBranchesIntoDto(repositories, branchesData);
    }

    /**
     * Retrieves all public repositories from gitHub api by username.
     *
     * @param userName The specific username of the gitHub user.
     * @return list of GitRepo objects with repositories data.
     * @throws UserNotFoundException if userName is not found.
     */
    List<GitRepo> getRepositoriesByUserName(String userName) {
        try {
            return restApiService.getRepositoriesByUsername(userName);
        } catch (HttpClientErrorException e) {
            throw new UserNotFoundException(userName);
        }
    }

    /**
     * Retrieves all branches from gitHub api by username and repository name.
     *
     * @param userName the specific username of the gitHub user.
     * @param repoName the name of specific public repository belonging to the user.
     * @return list of RepoBranch objects with repositories data anda specific name set.
     */
    List<RepoBranch> getBranchesToRepo(String userName, String repoName) {
        List<RepoBranch> branchList = restApiService.getBranchesByUsernameAndRepository(userName, repoName);
        branchList.forEach(branch -> branch.setRepoName(repoName));
        return branchList;
    }
}
