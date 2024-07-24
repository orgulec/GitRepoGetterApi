package org.example.gitrepogetterapi.api.exceptions;

public class WrongUserNameException extends RuntimeException {
    public WrongUserNameException() {
        super("Invalid user name.");
    }
}
