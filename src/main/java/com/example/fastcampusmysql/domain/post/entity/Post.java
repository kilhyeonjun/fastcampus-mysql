package com.example.fastcampusmysql.domain.post.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

  private final Long id;

  private final Long memberId;

  private final String contents;

  private final LocalDate createdDate;

  private final LocalDateTime createdAt;
  private final Long version;
  private Long likeCount;

  @Builder
  public Post(Long id, Long memberId, String contents, LocalDate createdDate,
      LocalDateTime createdAt, Long likeCount, Long version) {
    this.id = id;
    this.memberId = Objects.requireNonNull(memberId);
    this.contents = Objects.requireNonNull(contents);
    this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    this.likeCount = likeCount == null ? 0 : likeCount;
    this.version = version == null ? 0 : version;
  }

  public void increaseLikeCount() {
    likeCount += 1;
  }
}
