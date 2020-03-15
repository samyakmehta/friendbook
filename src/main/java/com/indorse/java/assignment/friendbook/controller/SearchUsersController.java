package com.indorse.java.assignment.friendbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indorse.java.assignment.friendbook.api.response.FriendBookUsersSearchResult;
import com.indorse.java.assignment.friendbook.service.SearchUserService;

@RestController
public class SearchUsersController {

  private final SearchUserService searchUserService;

  @Autowired
  public SearchUsersController(SearchUserService searchUserService) {
    this.searchUserService = searchUserService;
  }

  @GetMapping("/users/search")
  public FriendBookUsersSearchResult getSearchUsers(@RequestParam String searchQuery, @RequestParam int pageNumber,
      @RequestParam int pageSize) {

    return searchUserService.searchUserByQuery(searchQuery, pageNumber, pageSize);
  }
}
