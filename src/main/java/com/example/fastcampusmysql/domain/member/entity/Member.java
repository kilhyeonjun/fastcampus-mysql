package com.example.fastcampusmysql.domain.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class Member {

  final private static Long NAME_MAX_LENGTH = 10L;
  final private Long id;
  final private String email;

  final private LocalDate birthday;

  final private LocalDateTime createdAt;
  private String nickname;

  @Builder
  public Member(Long id, String nickname, String email, LocalDate birthday,
      LocalDateTime createdAt) {
    this.id = id;
    this.email = Objects.requireNonNull(email);
    this.birthday = Objects.requireNonNull(birthday);

    validateNickname(nickname);
    this.nickname = Objects.requireNonNull(nickname);

    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }

  public void changeNickname(String to) {
    Objects.requireNonNull(to);
    validateNickname(to);
    this.nickname = to;
  }

  private void validateNickname(String nickname) {
    Assert.isTrue(nickname.length() <= NAME_MAX_LENGTH, "최대 길이를 초과했습니다.");

  }
}
