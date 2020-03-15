package com.indorse.java.assignment.friendbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.indorse.java.assignment.friendbook.api.response.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionResponseHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = { FriendBookException.class })
  protected ExceptionResponse internalError(FriendBookException ex) {
    log.error(ex.getErrorMessage());
    return ExceptionResponse.builder().displayMessage(ex.getDisplayMessage())
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getErrorMessage()).build();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = { Exception.class })
  protected ExceptionResponse internalError(Exception ex) {
    log.error(ex.getMessage());
    return ExceptionResponse.builder().displayMessage("An unknown error occured.")
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = { UserCreationFailure.class })
  protected ExceptionResponse badRequest(FriendBookException ex) {
    return ExceptionResponse.builder().displayMessage(ex.getDisplayMessage()).errorCode(HttpStatus.BAD_REQUEST.value())
        .errorMessage(ex.getErrorMessage()).build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = { UserNotFoundException.class })
  protected ExceptionResponse notFound(FriendBookException ex) {
    return ExceptionResponse.builder().displayMessage(ex.getDisplayMessage()).errorCode(HttpStatus.NOT_FOUND.value())
        .errorMessage(ex.getErrorMessage()).build();
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(value = { AuthFailureException.class })
  protected ExceptionResponse unauthorized(FriendBookException ex) {
    return ExceptionResponse.builder().displayMessage(ex.getDisplayMessage()).errorCode(HttpStatus.UNAUTHORIZED.value())
        .errorMessage(ex.getErrorMessage()).build();
  }
}