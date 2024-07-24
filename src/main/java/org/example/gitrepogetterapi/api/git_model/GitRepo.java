package org.example.gitrepogetterapi.api.git_model;

public record GitRepo(String name, RepoOwner owner, boolean fork) {
}
