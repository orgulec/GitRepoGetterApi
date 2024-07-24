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

    @GetMapping("/{userName}")
    ResponseEntity<List<GitReposDto>> getByUserName(
            @RequestHeader("Accept") String header,
            @PathVariable String userName) {
        return ResponseEntity.ok(restService.getByUserName(userName));
    }

    @GetMapping("/")
    ResponseEntity<String> getWithNoUser() throws UserNotFoundException {
        throw new UserNotFoundException();
    }
}
