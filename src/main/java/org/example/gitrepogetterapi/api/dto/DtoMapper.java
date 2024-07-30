package org.example.gitrepogetterapi.api.dto;

import lombok.Data;
import org.example.gitrepogetterapi.api.git_model.GitRepo;
import org.example.gitrepogetterapi.api.git_model.RepoBranch;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class DtoMapper {

    /**
     * Creates one list of flat Dto objects from separated list of repositories and list of lists of branches.
     *
     * @param repositories list of GitRepo objects with repositories data.
     * @param branchesList list of RepoBranch objects lists with specific branches data.
     * @return merged list of GitReposDto,
     */
    public List<GitReposDto> mapReposAndBranchesIntoDto(List<GitRepo> repositories, List<List<RepoBranch>> branchesList) {

        List<GitReposDto> resultDtoList = repositories.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> new GitReposDto(repo.name(), repo.owner().login(), new ArrayList<>()))
                .collect(Collectors.toList());

        resultDtoList.forEach(dto -> branchesList
                .forEach(branches -> branches.stream()
                        .filter(branch -> branch.getRepoName().equals(dto.name()))
                        .forEach(branch -> dto.branches()
                                .add(new GitBranchDto(branch.getName(), branch.getCommit().sha()))
                        )));
        return resultDtoList;
    }
}
