package org.example.gitrepogetterapi.api.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found.");
    }
    public UserNotFoundException(String e) {
        super("User not found: " + e);
    }
}
