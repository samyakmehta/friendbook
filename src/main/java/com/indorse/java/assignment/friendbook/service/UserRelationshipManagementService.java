package com.indorse.java.assignment.friendbook.service;

import org.springframework.stereotype.Service;

import com.indorse.java.assignment.friendbook.model.db.FriendBookUser;
import com.indorse.java.assignment.friendbook.model.db.FriendBookUserMapping;
import com.indorse.java.assignment.friendbook.repository.UserMappingRepository;

@Service
public class UserRelationshipManagementService {

  private final UserMappingRepository userMappingRepository;

  public UserRelationshipManagementService(UserMappingRepository userMappingRepository) {
    this.userMappingRepository = userMappingRepository;
  }

  public FriendBookUserMapping addRelationShip(FriendBookUser from, FriendBookUser to) {

    FriendBookUserMapping mapping = userMappingRepository.findByFriendbookUserAndMapsTo(from, to);
    if (mapping == null) {
      mapping = userMappingRepository.findByFriendbookUserAndMapsTo(to, from);
      if (mapping == null) {
        return userMappingRepository.save(FriendBookUserMapping.builder().friendbookUser(from).mapsTo(to).build());
      }
    }
    return mapping;
  }

  public void deleteRelationShip(FriendBookUser from, FriendBookUser to) {

    FriendBookUserMapping mapping = userMappingRepository.findByFriendbookUserAndMapsTo(from, to);
    if (mapping != null) {
      userMappingRepository.deleteById(mapping.getId());
    }
    mapping = userMappingRepository.findByFriendbookUserAndMapsTo(to, from);
    if (mapping != null) {
      userMappingRepository.deleteById(mapping.getId());
    }
  }
}
