package com.indorse.java.assignment.friendbook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.indorse.java.assignment.friendbook.model.db.FriendBookUser;
import com.indorse.java.assignment.friendbook.model.db.FriendBookUserMapping;

public interface UserMappingRepository
    extends CrudRepository<FriendBookUserMapping, Integer>, PagingAndSortingRepository<FriendBookUserMapping, Integer> {

  FriendBookUserMapping findByFriendbookUserAndMapsTo(FriendBookUser from, FriendBookUser to);
}
