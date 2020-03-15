package com.indorse.java.assignment.friendbook.service;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.indorse.java.assignment.friendbook.exceptions.AuthFailureException;

@Service
public class JwtService {

  public String generateJWT(String userId, String passwordHash) {

    Algorithm algorithm = Algorithm.HMAC256(passwordHash);
    long timeInMillis = Calendar.getInstance().getTimeInMillis();
    long validTill = timeInMillis + TimeUnit.HOURS.toMillis(24);
    return JWT.create().withIssuer("friendBookAuth").withSubject(userId).withClaim(PublicClaims.EXPIRES_AT, validTill)
        .sign(algorithm);
  }

  public boolean validateJWT(String userId, String passwordHash, String jwt) throws AuthFailureException {

    try {
      Algorithm algorithm = Algorithm.HMAC256(passwordHash);
      JWTVerifier verifier = JWT.require(algorithm).withIssuer("friendBookAuth").withSubject(userId).build();
      DecodedJWT decodedJwt = verifier.verify(jwt);
      long expiresAt = decodedJwt.getClaim(PublicClaims.EXPIRES_AT).asLong();
      if (expiresAt < Calendar.getInstance().getTimeInMillis()) {
        throw new AuthFailureException("The token has expired", "The token has expired");
      }
      return true;
    } catch (Exception ex) {
      throw new AuthFailureException("Invalid Auth Token", "Invalid Auth Token");
    }
  }
}
