package org.example.gitrepogetterapi.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GitReposDto {

    private final String name;
    private final String owner;
    private List<GitBranchDto> branches = new ArrayList<>();
}
