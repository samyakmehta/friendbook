package com.indorse.java.assignment.friendbook.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.indorse.java.assignment.friendbook.exceptions.AuthFailureException;
import com.indorse.java.assignment.friendbook.model.db.UserSecurityInfo;
import com.indorse.java.assignment.friendbook.repository.UserSecurityInfoRepository;
import com.indorse.java.assignment.friendbook.util.CommonUtil;

@Service
public class ValidateUserAuthService {

  private final UserSecurityInfoRepository userSecurityInfoRepository;
  private final JwtService jwtService;

  public ValidateUserAuthService(UserSecurityInfoRepository userSecurityInfoRepository, JwtService jwtService) {
    this.userSecurityInfoRepository = userSecurityInfoRepository;
    this.jwtService = jwtService;
  }

  public String validateUserAuth(HttpServletRequest httpServletRequest) throws AuthFailureException {

    String authHeader = httpServletRequest.getHeader("Authorization");
    if (CommonUtil.isEmpty(authHeader)) {
      throw new AuthFailureException("Invalid Auth Header",
          "Invalid Auth Header. The valid format is 'Bearer <token>'");
    }

    String[] headerTokens = authHeader.split(" ");
    if (headerTokens == null || headerTokens.length != 2 || CommonUtil.isEmpty(headerTokens[0])
        || !headerTokens[0].equalsIgnoreCase("Bearer") || CommonUtil.isEmpty(headerTokens[1])) {
      throw new AuthFailureException("Invalid Auth Header",
          "Invalid Auth Header. The valid format is 'Bearer <token>'");
    }

    String token = headerTokens[1];
    DecodedJWT decodedJwt = JWT.decode(token);
    String userId = decodedJwt.getSubject();

    UserSecurityInfo userSecurityInfo = userSecurityInfoRepository.findById(UUID.fromString(userId)).orElse(null);
    if (userSecurityInfo == null) {
      throw new AuthFailureException("The user does not exist", "The user does not exist");
    }

    jwtService.validateJWT(userId, userSecurityInfo.getPasswordHash(), token);
    return userId;
  }
}
