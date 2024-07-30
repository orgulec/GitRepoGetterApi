package org.example.gitrepogetterapi.api.dto;

import java.util.List;

public record GitReposDto (
    String name,
    String owner,
    List<GitBranchDto> branches){

    @Override
    public String name() {
        return name;
    }

    @Override
    public String owner() {
        return owner;
    }

    @Override
    public List<GitBranchDto> branches() {
        return branches;
    }
}
