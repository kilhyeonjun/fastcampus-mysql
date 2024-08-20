package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostReadService {

  private final PostRepository postRepository;

  public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
    return postRepository.groupByCreatedDate(request);
  }

  public Page<Post> getPosts(Long memberId, Pageable pageable) {
    return postRepository.findAllByMemberId(memberId, pageable);
  }
}
