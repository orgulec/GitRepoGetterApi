package org.example.gitrepogetterapi.api.dto;

import org.example.gitrepogetterapi.api.git_model.BranchCommit;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.example.gitrepogetterapi.api.git_model.RepoOwner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoMapperTest {

    @Autowired
    DtoMapper dtoMapper;

    @Test
    void mapReposAndBranchesIntoDto() {
        //given
        String userName = "testUser";
        String repoName = "testRepo";
        String branchName1 = "branch1";
        String branchName2 = "branch2";
        String commitSha1 = "sha1";
        String commitSha2 = "sha2";

        GitRepo mockRepo = new GitRepo(repoName, new RepoOwner(userName), false);
        RepoBranch mockBranch1 = new RepoBranch(branchName1, new BranchCommit(commitSha1));
        RepoBranch mockBranch2 = new RepoBranch(branchName2, new BranchCommit(commitSha2));
        mockBranch1.setRepoName(repoName);
        mockBranch2.setRepoName(repoName);

        List<GitRepo> mockRepositories = List.of(mockRepo);
        List<List<RepoBranch>> mockBranchesList = List.of(List.of(mockBranch1, mockBranch2));

        GitReposDto mockRepoDto = new GitReposDto(repoName, userName, List.of(
                new GitBranchDto(branchName1, commitSha1),
                new GitBranchDto(branchName2, commitSha2)));
        List<GitReposDto> expectedDtoList = List.of(mockRepoDto);
        //when

        //then
        List<GitReposDto> result = dtoMapper.mapReposAndBranchesIntoDto(mockRepositories, mockBranchesList);
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(expectedDtoList.size(), result.size()),
                ()->assertEquals(expectedDtoList.getFirst().branches().size(), result.getFirst().branches().size())
        );
    }
}