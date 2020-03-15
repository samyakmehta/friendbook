package com.indorse.java.assignment.friendbook.exceptions;

public class AuthFailureException extends FriendBookException {

  private static final long serialVersionUID = 1L;

  public AuthFailureException(String errorMessage, String displayMessage) {
    setDisplayMessage(displayMessage);
    setErrorMessage(errorMessage);
    setErrorCode(401);
  }
}
