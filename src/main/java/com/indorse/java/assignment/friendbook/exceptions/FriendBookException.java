package com.indorse.java.assignment.friendbook.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendBookException extends Exception {

  private static final long serialVersionUID = 1L;
  private String errorMessage;
  private String displayMessage;
  private int errorCode;
}
