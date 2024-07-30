package org.example.gitrepogetterapi.api.dto;

public record GitBranchDto(
        String name,
        String commitSha) {

    @Override
    public String name() {
        return name;
    }

    @Override
    public String commitSha() {
        return commitSha;
    }
}
