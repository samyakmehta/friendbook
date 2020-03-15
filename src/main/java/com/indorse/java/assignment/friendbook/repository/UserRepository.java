package com.indorse.java.assignment.friendbook.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.indorse.java.assignment.friendbook.model.db.FriendBookUser;

public interface UserRepository
    extends CrudRepository<FriendBookUser, UUID>, PagingAndSortingRepository<FriendBookUser, UUID> {

  FriendBookUser findByEmailId(String emailId);

  List<FriendBookUser> findByNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String nameInput,
      String lastNameInput, Pageable pageable);

  FriendBookUser save(FriendBookUser user);
}
