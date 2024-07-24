package org.example.gitrepogetterapi.api.exceptions;

public class NoSuchRepositoriesException extends RuntimeException {
    public NoSuchRepositoriesException(String userName) {
        super("User " + userName + " has no public repositories to get.");
    }
}
