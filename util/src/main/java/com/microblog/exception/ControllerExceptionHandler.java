package com.microblog.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody ErrInfo handleIllegalArgumentException(ServerHttpRequest request, Exception ex) {
        String path = request.getPath().pathWithinApplication().value();
        return new ErrInfo(HttpStatus.BAD_REQUEST, path, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalAccessException.class)
    public @ResponseBody ErrInfo handleIllegalAccessException(ServerHttpRequest request, Exception ex) {
        String path = request.getPath().pathWithinApplication().value();
        return new ErrInfo(HttpStatus.NOT_FOUND, path, ex.getMessage());
    }

}