package org.example.gitrepogetterapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.NoSuchRepositoriesException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.exceptions.WrongUserNameException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Controller
@RequiredArgsConstructor
class GitRepoController {

    private final GitReposService gitReposService;

    @GetMapping("/{userName}")
    ResponseEntity<List<GitReposDto>> getGitRepoByUser(
            @RequestHeader("Accept") String header,
            @PathVariable String userName)
            throws UserNotFoundException, WrongUserNameException, NoSuchRepositoriesException {
        List<GitReposDto> result = gitReposService.getReposAndBranchesFromApiByUser(userName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/")
    ResponseEntity<String> getWithNoUser() throws UserNotFoundException {
        throw new UserNotFoundException();
    }

}
