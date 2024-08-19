package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreateFollowMemberUsecase;
import com.example.fastcampusmysql.application.usecase.GetFollowingMembersUsecase;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follows")
public class FollowController {

  private final CreateFollowMemberUsecase createFollowMemberUsecase;

  private final GetFollowingMembersUsecase getFollowingMembersUsecase;

  @PostMapping("/{fromId}/{toId}")
  public void create(@PathVariable Long fromId, @PathVariable Long toId) {
    createFollowMemberUsecase.execute(fromId, toId);
  }

  @GetMapping("/members/{fromId}")
  public List<MemberDto> getFollowings(@PathVariable Long fromId) {
    return getFollowingMembersUsecase.execute(fromId);
  }
}
