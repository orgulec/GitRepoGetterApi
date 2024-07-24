package org.example.gitrepogetterapi.api.exceptions;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException() {
        super("Invalid user name.");
    }
}
