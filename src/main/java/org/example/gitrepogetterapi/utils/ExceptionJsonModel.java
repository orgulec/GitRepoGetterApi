package org.example.gitrepogetterapi.utils;

import org.springframework.http.HttpStatus;

public record ExceptionJsonModel(HttpStatus status, String message) {}
