package org.example.gitrepogetterapi.api.dto;

import lombok.Data;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;

import java.util.ArrayList;
import java.util.List;

@Data
public class DtoMapper {
    public List<GitReposDto> mapReposAndBranchesIntoDto(List<GitRepo> repositories, List<List<RepoBranch>> branchesData) {
        List<GitReposDto> resultDtoList = new ArrayList<>();

        repositories.stream()
                .filter(repo -> !repo.fork())
                .forEach(repo -> resultDtoList
                        .add(new GitReposDto(repo.name(), repo.owner().login())));

        resultDtoList.forEach(dto -> {
            branchesData.forEach(branches -> {
                branches.stream()
                        .filter(branch -> branch.getRepoName().equals(dto.getName()))
                        .forEach(branch -> dto.getBranches()
                                .add(new GitBranchDto(branch.getName(), branch.getCommit().sha()))
                        );
            });
        });
        return resultDtoList;
    }
}
