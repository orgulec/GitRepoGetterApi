package org.example.gitrepogetterapi.utils;

import org.example.gitrepogetterapi.api.exceptions.NoSuchRepositoriesException;
import org.example.gitrepogetterapi.api.exceptions.UserNotFoundException;
import org.example.gitrepogetterapi.api.exceptions.WrongUserNameException;
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
    @ExceptionHandler(WrongUserNameException.class)
    public ResponseEntity<ExceptionJsonModel> EmptyUserExceptionHandler(WrongUserNameException ex) {
        ExceptionJsonModel exJson = new ExceptionJsonModel(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(exJson, headers, exJson.status());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchRepositoriesException.class)
    public ResponseEntity<ExceptionJsonModel> NoSuchRepositoriesExceptionHandler(NoSuchRepositoriesException ex) {
        ExceptionJsonModel exJson = new ExceptionJsonModel(HttpStatus.NO_CONTENT, ex.getMessage());
        return new ResponseEntity<>(exJson, headers, exJson.status());
    }

}
