package com.indorse.java.assignment.friendbook.model.db;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
@Entity(name = "friendbookUser")
@Table(name = "friendbookUser")
public class FriendBookUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
  @GenericGenerator(name = "pg-uuid", strategy = "uuid2", parameters = @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "com.vladmihalcea.book.hpjp.hibernate.identifier.uuid.PostgreSQLUUIDGenerationStrategy"))
  @Column(columnDefinition = "uuid")
  private UUID userId;

  @Column(columnDefinition = "character varying(100)", nullable = false)
  private String name;

  @Column(nullable = false, columnDefinition = "character varying(100)")
  private String lastName;

  @Column(columnDefinition = "character varying(100)", nullable = false, unique = true)
  private String emailId;
}
