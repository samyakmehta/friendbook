package com.indorse.java.assignment.friendbook.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indorse.java.assignment.friendbook.api.request.AddFriendRequest;
import com.indorse.java.assignment.friendbook.api.request.FriendBookCreateUserRequest;
import com.indorse.java.assignment.friendbook.api.request.FriendBookUserAuthenticateRequest;
import com.indorse.java.assignment.friendbook.api.request.RemoveFriendRequest;
import com.indorse.java.assignment.friendbook.api.response.FriendBookUserAuthResponse;
import com.indorse.java.assignment.friendbook.api.response.FriendBookUserResponse;
import com.indorse.java.assignment.friendbook.exceptions.AuthFailureException;
import com.indorse.java.assignment.friendbook.exceptions.UserCreationFailure;
import com.indorse.java.assignment.friendbook.exceptions.UserNotFoundException;
import com.indorse.java.assignment.friendbook.model.db.FriendBookUser;
import com.indorse.java.assignment.friendbook.model.db.UserSecurityInfo;
import com.indorse.java.assignment.friendbook.repository.UserRepository;
import com.indorse.java.assignment.friendbook.repository.UserSecurityInfoRepository;
import com.indorse.java.assignment.friendbook.util.CommonUtil;

@Service
public class UserActionsService {

  private final UserRepository userRepository;
  private final UserSecurityInfoRepository userSecurityInfoRepository;
  private final JwtService jwtService;
  private final UserRelationshipManagementService userRelationshipManagementService;

  @Autowired
  public UserActionsService(UserRepository userRepository, UserSecurityInfoRepository userSecurityInfoRepository,
      JwtService jwtService, UserRelationshipManagementService userRelationshipManagementService) {
    this.userRepository = userRepository;
    this.userSecurityInfoRepository = userSecurityInfoRepository;
    this.jwtService = jwtService;
    this.userRelationshipManagementService = userRelationshipManagementService;
  }

  public FriendBookUserResponse findUserById(String id) throws UserNotFoundException {

    UUID uuid = null;
    try {
      uuid = UUID.fromString(id);
    } catch (Exception ex) {
      throw new UserNotFoundException("User with id " + id + " does not exist",
          "User with id " + id + " does not exist");
    }
    FriendBookUser friendBookUser = userRepository.findById(uuid).orElse(null);
    if (friendBookUser == null) {
      throw new UserNotFoundException("User with id " + id + " does not exist",
          "User with id " + id + " does not exist");
    }
    return FriendBookUserResponse.builder().id(friendBookUser.getUserId().toString())
        .emailId(friendBookUser.getEmailId()).name(friendBookUser.getName()).lastName(friendBookUser.getLastName())
        .build();
  }

  public FriendBookUserResponse findUserByEmailId(String emailId) throws UserNotFoundException {

    FriendBookUser friendBookUser = userRepository.findByEmailId(emailId);
    if (friendBookUser == null) {
      throw new UserNotFoundException("User with email id " + emailId + " does not exist",
          "User with email id " + emailId + " does not exist");
    }
    return FriendBookUserResponse.builder().id(friendBookUser.getUserId().toString())
        .emailId(friendBookUser.getEmailId()).name(friendBookUser.getName()).lastName(friendBookUser.getLastName())
        .build();
  }

  public FriendBookUserResponse createFriendBookUser(FriendBookCreateUserRequest fbCreateUserRequest)
      throws NoSuchAlgorithmException, InvalidKeySpecException, UserCreationFailure {

    validateUserCreationRequest(fbCreateUserRequest);

    String salt = CommonUtil.generateNewSalt();
    String passwordHash = CommonUtil.generateSaltPasswordHash(salt, fbCreateUserRequest.getPassword());

    FriendBookUser friendBookUser = FriendBookUser.builder().emailId(fbCreateUserRequest.getEmailId())
        .name(fbCreateUserRequest.getName()).lastName(fbCreateUserRequest.getLastName()).build();

    friendBookUser = userRepository.save(friendBookUser);
    UserSecurityInfo userSecurityInfo = UserSecurityInfo.builder().passwordHash(passwordHash).salt(salt)
        .friendbookUser(friendBookUser).build();
    userSecurityInfo = userSecurityInfoRepository.save(userSecurityInfo);

    return FriendBookUserResponse.builder().id(friendBookUser.getUserId().toString())
        .emailId(friendBookUser.getEmailId()).name(friendBookUser.getName()).lastName(friendBookUser.getLastName())
        .build();
  }

  public FriendBookUserAuthResponse authenticateUser(FriendBookUserAuthenticateRequest fbUserAuthRequest)
      throws NoSuchAlgorithmException, InvalidKeySpecException, UserNotFoundException, AuthFailureException,
      UserCreationFailure {

    validateAuthRequest(fbUserAuthRequest);

    FriendBookUser friendBookUser = userRepository.findByEmailId(fbUserAuthRequest.getEmailId());
    if (friendBookUser == null) {
      throw new UserNotFoundException("User with email id " + fbUserAuthRequest.getEmailId() + " does not exist",
          "User with email id " + fbUserAuthRequest.getEmailId() + " does not exist");
    }

    UserSecurityInfo userSecurityInfo = userSecurityInfoRepository.findById(friendBookUser.getUserId()).orElse(null);
    if (userSecurityInfo == null) {
      throw new UserNotFoundException("Auth credentials for " + fbUserAuthRequest.getEmailId() + " do not exist",
          "Auth credentials for " + fbUserAuthRequest.getEmailId() + " do not exist");
    }

    String existingSalt = userSecurityInfo.getSalt();
    String existingPasswordHash = userSecurityInfo.getPasswordHash();

    String providedPasswordHash = CommonUtil.generateSaltPasswordHash(existingSalt, fbUserAuthRequest.getPassword());
    if (existingPasswordHash.equals(providedPasswordHash)) {

      String jwt = jwtService.generateJWT(friendBookUser.getUserId().toString(), existingPasswordHash);
      return FriendBookUserAuthResponse.builder().token(jwt).emailId(fbUserAuthRequest.getEmailId())
          .expiresAt(new Date().getTime() + 86400000).build();
    } else {
      throw new AuthFailureException("Invalid username or password", "Invalid username or password");
    }
  }

  public void addFriendForUser(String userId, AddFriendRequest addFriendRequest) throws UserNotFoundException {

    UUID userUUID = null;
    UUID requestedUUID = null;
    String requestedFriendId = addFriendRequest.getRequestedFriendId();
    try {
      userUUID = UUID.fromString(userId);
    } catch (Exception ex) {
      throw new UserNotFoundException("User with id " + userId + " does not exist",
          "User with id " + userId + " does not exist");
    }
    try {
      requestedUUID = UUID.fromString(requestedFriendId);
    } catch (Exception ex) {
      throw new UserNotFoundException("User with id " + requestedFriendId + " does not exist",
          "User with id " + requestedFriendId + " does not exist");
    }

    FriendBookUser friendBookUser = userRepository.findById(userUUID).orElse(null);
    if (friendBookUser == null) {
      throw new UserNotFoundException("User with id " + userId + " does not exist",
          "User with id " + userId + " does not exist");
    }

    FriendBookUser requestedFriendUser = userRepository.findById(requestedUUID).orElse(null);
    if (requestedFriendUser == null) {
      throw new UserNotFoundException("User with id " + requestedFriendId + " does not exist",
          "User with id " + requestedFriendId + " does not exist");
    }

    userRelationshipManagementService.addRelationShip(friendBookUser, requestedFriendUser);
  }

  public void removeFriendForUser(String userId, RemoveFriendRequest removeFriendRequest) throws UserNotFoundException {

    FriendBookUser friendBookUser = userRepository.findById(UUID.fromString(userId)).orElse(null);
    if (friendBookUser == null) {
      throw new UserNotFoundException("User with id " + userId + " does not exist",
          "User with id " + userId + " does not exist");
    }

    String requestedFriendId = removeFriendRequest.getRequestedFriendId();
    FriendBookUser requestedFriendUser = userRepository.findById(UUID.fromString(requestedFriendId)).orElse(null);
    if (requestedFriendUser == null) {
      throw new UserNotFoundException("User with id " + requestedFriendId + " does not exist",
          "User with id " + requestedFriendId + " does not exist");
    }

    userRelationshipManagementService.deleteRelationShip(friendBookUser, requestedFriendUser);
  }

  private void validateUserCreationRequest(FriendBookCreateUserRequest fbCreateUserRequest) throws UserCreationFailure {
    if (CommonUtil.isEmpty(fbCreateUserRequest.getEmailId())) {
      throw new UserCreationFailure("Email id cannot be empty", "Email id cannot be empty");
    }

    if (CommonUtil.isEmpty(fbCreateUserRequest.getName())) {
      throw new UserCreationFailure("Name cannot be empty", "Name cannot be empty");
    }

    if (CommonUtil.isEmpty(fbCreateUserRequest.getLastName())) {
      throw new UserCreationFailure("Last name cannot be empty", "Last name id cannot be empty");
    }

    if (CommonUtil.isEmpty(fbCreateUserRequest.getPassword())) {
      throw new UserCreationFailure("Password cannot be empty", "Password cannot be empty");
    }
  }

  private void validateAuthRequest(FriendBookUserAuthenticateRequest authRequest)
      throws UserCreationFailure, AuthFailureException {
    if (CommonUtil.isEmpty(authRequest.getEmailId())) {
      throw new AuthFailureException("Email id cannot be empty", "Email id cannot be empty");
    }

    if (CommonUtil.isEmpty(authRequest.getPassword())) {
      throw new AuthFailureException("Password cannot be empty", "Password cannot be empty");
    }
  }

}
