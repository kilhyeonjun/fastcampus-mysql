package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.utill.CursorRequest;
import com.example.fastcampusmysql.utill.PageCursor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostReadService {

  private final PostRepository postRepository;

  private static long getNextKey(List<Post> posts) {
    return posts.stream()
        .mapToLong(Post::getId)
        .min()
        .orElse(CursorRequest.NONE_KEY);
  }

  public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
    return postRepository.groupByCreatedDate(request);
  }

  public Page<Post> getPosts(Long memberId, Pageable pageable) {
    return postRepository.findAllByMemberId(memberId, pageable);
  }

  public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
    var posts = findAllBy(memberId, cursorRequest);
    var nextKey = getNextKey(posts);

    return new PageCursor<>(cursorRequest.next(nextKey), posts);
  }

  public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
    var posts = findAllBy(memberIds, cursorRequest);
    var nextKey = getNextKey(posts);

    return new PageCursor<>(cursorRequest.next(nextKey), posts);
  }

  private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
    if (cursorRequest.hasKey()) {
      return postRepository.findAllByLessThanIdAndMemberIdOrderByIdDesc(cursorRequest.key(),
          memberId, cursorRequest.size());
    }

    return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
  }

  private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {
    if (cursorRequest.hasKey()) {
      return postRepository.findAllByLessThanIdAndInMemberIdOrderByIdDesc(cursorRequest.key(),
          memberIds, cursorRequest.size());
    }

    return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberIds, cursorRequest.size());
  }
}
