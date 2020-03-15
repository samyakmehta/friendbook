package com.indorse.java.assignment.friendbook.model.db;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "userSecurityInfo")
@Table(name = "userSecurityInfo")
public class UserSecurityInfo {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID userId;

  @Column(columnDefinition = "character varying(100)", nullable = false)
  private String salt;

  @Column(columnDefinition = "character varying(500)", nullable = false)
  private String passwordHash;

  @OneToOne
  @MapsId
  private FriendBookUser friendbookUser;
}
