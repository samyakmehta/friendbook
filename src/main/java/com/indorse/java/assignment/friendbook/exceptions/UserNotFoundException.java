package com.indorse.java.assignment.friendbook.exceptions;

public class UserNotFoundException extends FriendBookException {

  private static final long serialVersionUID = 1L;

  public UserNotFoundException(String errorMessage, String displayMessage) {
    setDisplayMessage(displayMessage);
    setErrorMessage(errorMessage);
    setErrorCode(401);
  }
}
