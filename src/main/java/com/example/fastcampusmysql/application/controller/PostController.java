package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreatePostLikeUsecase;
import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetTimelinePostsUsecase;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.utill.CursorRequest;
import com.example.fastcampusmysql.utill.PageCursor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostWriteService postWriteService;
  private final PostReadService postReadService;
  private final GetTimelinePostsUsecase getTimelinePostsUsecase;
  private final CreatePostUsecase createPostUsecase;
  private final CreatePostLikeUsecase createPostLikeUsecase;

  @PostMapping
  public Long create(@RequestBody PostCommand command) {
    return createPostUsecase.execute(command);
  }

  @GetMapping("/daily-post-counts")
  public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
    return postReadService.getDailyPostCounts(request);
  }

  @GetMapping("/members/{memberId}")
  public Page<PostDto> getPosts(
      @PathVariable Long memberId,
      Pageable pageable
  ) {
    return postReadService.getPosts(memberId, pageable);
  }

  @GetMapping("/members/{memberId}/by-cursor")
  public PageCursor<Post> getPostsByCursor(
      @PathVariable Long memberId,
      CursorRequest cursorRequest
  ) {
    return postReadService.getPosts(memberId, cursorRequest);
  }

  @GetMapping("/members/{memberId}/timeline")
  public PageCursor<Post> getTimeline(
      @PathVariable Long memberId,
      CursorRequest cursorRequest
  ) {
    return getTimelinePostsUsecase.executeByTimeline(memberId, cursorRequest);
  }

  @PostMapping("/{postId}/like/v1")
  public void likePost(@PathVariable Long postId) {
//    postWriteService.likePost(postId);
    postWriteService.likePostByOptimisticLock(postId);
  }

  @PostMapping("/{postId}/like/v2")
  public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId) {
    createPostLikeUsecase.execute(postId, memberId);
  }
}
