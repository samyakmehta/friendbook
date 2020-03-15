package com.indorse.java.assignment.friendbook.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.indorse.java.assignment.friendbook.api.request.AddFriendRequest;
import com.indorse.java.assignment.friendbook.api.request.FriendBookCreateUserRequest;
import com.indorse.java.assignment.friendbook.api.request.FriendBookUserAuthenticateRequest;
import com.indorse.java.assignment.friendbook.api.request.RemoveFriendRequest;
import com.indorse.java.assignment.friendbook.api.response.FriendBookUserAuthResponse;
import com.indorse.java.assignment.friendbook.api.response.FriendBookUserResponse;
import com.indorse.java.assignment.friendbook.exceptions.AuthFailureException;
import com.indorse.java.assignment.friendbook.exceptions.UserCreationFailure;
import com.indorse.java.assignment.friendbook.exceptions.UserNotFoundException;
import com.indorse.java.assignment.friendbook.service.UserActionsService;
import com.indorse.java.assignment.friendbook.service.ValidateUserAuthService;

@RestController
public class UserController {

  private final UserActionsService userActionsService;
  private final ValidateUserAuthService authService;

  @Autowired
  public UserController(UserActionsService userActionsService, ValidateUserAuthService authService) {
    this.userActionsService = userActionsService;
    this.authService = authService;
  }

  @GetMapping("/user/{id}")
  public FriendBookUserResponse getUser(@PathVariable String id) throws UserNotFoundException {
    return userActionsService.findUserById(id);
  }

  @GetMapping("/user/email/{emailId}")
  public FriendBookUserResponse getUserByEmailId(@PathVariable String emailId) throws UserNotFoundException {
    return userActionsService.findUserByEmailId(emailId);
  }

  @PostMapping("/user/create")
  public FriendBookUserResponse createUser(@RequestBody FriendBookCreateUserRequest fbCreateUserRequest)
      throws NoSuchAlgorithmException, InvalidKeySpecException, UserCreationFailure {
    return userActionsService.createFriendBookUser(fbCreateUserRequest);
  }

  @PostMapping("/user/authenticate")
  public FriendBookUserAuthResponse authenticateUser(@RequestBody FriendBookUserAuthenticateRequest fbUserAuthRequest)
      throws NoSuchAlgorithmException, InvalidKeySpecException, UserNotFoundException, AuthFailureException,
      UserCreationFailure {
    return userActionsService.authenticateUser(fbUserAuthRequest);
  }

  @PostMapping("/user/addFriend")
  public void addFriend(@RequestBody AddFriendRequest addFriendRequest, HttpServletRequest httpServlet)
      throws AuthFailureException, UserNotFoundException {

    String userId = authService.validateUserAuth(httpServlet);
    userActionsService.addFriendForUser(userId, addFriendRequest);
  }

  @PostMapping("/user/removeFriend")
  public void addFriend(@RequestBody RemoveFriendRequest removeFriendRequest, HttpServletRequest httpServlet)
      throws AuthFailureException, UserNotFoundException {

    String userId = authService.validateUserAuth(httpServlet);
    userActionsService.removeFriendForUser(userId, removeFriendRequest);
  }
}
