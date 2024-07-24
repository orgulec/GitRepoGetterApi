package org.example.gitrepogetterapi.api.dto;

import lombok.Data;

@Data
public class GitBranchDto {

    private final String name;
    private final String commit_sha;
}
