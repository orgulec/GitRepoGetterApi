package org.example.gitrepogetterapi.utils;

import org.example.gitrepogetterapi.api.exceptions.InvalidUserNameException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    HttpHeaders headers;

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionJsonModel> UserNotFoundExceptionHandler(UserNotFoundException ex) {
        ExceptionJsonModel exJson = new ExceptionJsonModel(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(exJson, headers, exJson.status());
    }

    @ResponseBody
    @ExceptionHandler(InvalidUserNameException.class)
    public ResponseEntity<ExceptionJsonModel> EmptyUserExceptionHandler(InvalidUserNameException ex) {
        ExceptionJsonModel exJson = new ExceptionJsonModel(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(exJson, headers, exJson.status());
    }

}
