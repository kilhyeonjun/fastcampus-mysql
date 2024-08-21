package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.post.service.PostLikeWriteService;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreatePostLikeUsecase {

  private final PostReadService postReadService;
  private final MemberReadService memberReadService;
  private final PostLikeWriteService postLikeWriteService;

  public Long execute(Long postId, Long memberId) {
    var post = postReadService.getPost(postId);
    var member = memberReadService.getMember(memberId);
    return postLikeWriteService.create(post, member);
  }
}
