package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.TimelineReadService;
import com.example.fastcampusmysql.utill.CursorRequest;
import com.example.fastcampusmysql.utill.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetTimelinePostsUsecase {

  private final FollowReadService followReadService;
  private final PostReadService postReadService;
  private final TimelineReadService timelineReadService;

  public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
    var followings = followReadService.getFollowings(memberId);
    var followingMemberIds = followings.stream()
        .map(Follow::getToMemberId)
        .toList();

    return postReadService.getPosts(followingMemberIds, cursorRequest);
  }

  public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
    var timelines = timelineReadService.getTimelines(memberId, cursorRequest);
    var postIds = timelines.body().stream()
        .map(Timeline::getPostId)
        .toList();
    var posts = postReadService.getPosts(postIds);

    return new PageCursor<>(timelines.nextCursorRequest(), posts);
  }
}
