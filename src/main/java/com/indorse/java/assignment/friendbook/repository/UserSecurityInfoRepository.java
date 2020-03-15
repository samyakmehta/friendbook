package com.indorse.java.assignment.friendbook.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.indorse.java.assignment.friendbook.model.db.UserSecurityInfo;

public interface UserSecurityInfoRepository extends CrudRepository<UserSecurityInfo, UUID> {

  UserSecurityInfo save(UserSecurityInfo userSecurityInfo);
}
