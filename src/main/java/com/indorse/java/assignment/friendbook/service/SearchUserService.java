package com.indorse.java.assignment.friendbook.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.indorse.java.assignment.friendbook.api.response.FriendBookUserResponse;
import com.indorse.java.assignment.friendbook.api.response.FriendBookUsersSearchResult;
import com.indorse.java.assignment.friendbook.model.db.FriendBookUser;
import com.indorse.java.assignment.friendbook.repository.UserRepository;
import com.indorse.java.assignment.friendbook.util.CommonUtil;

@Service
public class SearchUserService {

  private final UserRepository userRepository;

  public SearchUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public FriendBookUsersSearchResult searchUserByQuery(String searchQuery, int pageNumber, int resultSize) {

    searchQuery = (CommonUtil.isEmpty(searchQuery)) ? "" : searchQuery;
    pageNumber = (pageNumber < 0) ? 0 : pageNumber;
    resultSize = (resultSize > 0) ? resultSize : 20;
    List<FriendBookUser> friendBookUsers = userRepository.findByNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
        searchQuery, searchQuery, PageRequest.of(pageNumber, resultSize));

    List<FriendBookUserResponse> results = friendBookUsers.parallelStream().map(fbUser -> {
      return FriendBookUserResponse.builder().emailId(fbUser.getEmailId()).name(fbUser.getName())
          .lastName(fbUser.getLastName()).id(fbUser.getUserId().toString()).build();
    }).collect(Collectors.toList());

    return FriendBookUsersSearchResult.builder().pageNumber(pageNumber).results(results).resultSize(results.size())
        .build();
  }
}
