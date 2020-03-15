package com.indorse.java.assignment.friendbook.exceptions;

public class UserCreationFailure extends FriendBookException {

  private static final long serialVersionUID = 1L;

  public UserCreationFailure(String errorMessage, String displayMessage) {
    setDisplayMessage(displayMessage);
    setErrorMessage(errorMessage);
    setErrorCode(400);
  }
}
