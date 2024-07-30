package org.example.gitrepogetterapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.gitrepogetterapi.api.dto.GitReposDto;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
class GitRestController {

    private final GitRestService restService;

    /**
     * Retrieves all public repositories from gitHub api by username.
     *
     * @param userName The specific username of the gitHub user.
     * @return ResponseEntity containing a list of repositories and their branches set in Dto object.
     * @throws UserNotFoundException with status 404 if userName is not found or InvalidUserNameException with status 400 if userName is not correct
     */
    @GetMapping("/{userName}")
    ResponseEntity<List<GitReposDto>> getByUserName(
            @RequestHeader("Accept") String header,
            @PathVariable String userName) {
        return ResponseEntity.ok(restService.getByUserName(userName));
    }

    /**
     * Empty endpoint when there is no @param userName
     *
     * @throws UserNotFoundException with status 404
     */
    @GetMapping("/")
    void getWithNoUser() throws UserNotFoundException {
        throw new UserNotFoundException();
    }
}
