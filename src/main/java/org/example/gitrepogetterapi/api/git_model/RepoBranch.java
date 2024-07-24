package org.example.gitrepogetterapi.api.git_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RepoBranch {
    private final String name;
    private final BranchCommit commit;
    @JsonIgnore
    private String repoName;

    public RepoBranch(String name, BranchCommit commit) {
        this.name = name;
        this.commit = commit;
    }

}
