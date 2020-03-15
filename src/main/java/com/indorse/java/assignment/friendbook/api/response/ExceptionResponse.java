package com.indorse.java.assignment.friendbook.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {

  private int errorCode;
  private String errorMessage;
  private String displayMessage;
}
