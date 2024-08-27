package org.example.gitrepogetterapi.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException() {
        super("Invalid user name.");
    }
}
