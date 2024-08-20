package com.example.fastcampusmysql.domain.post;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.utill.PostFixtureFactory;
import java.time.LocalDate;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
class PostBulkInsertTest {

  @Autowired
  private PostRepository postRepository;

  @Test
  void bulkInsert() {
    var easyRandom = PostFixtureFactory.get(
        3L,
        LocalDate.of(2024, 7, 20),
        LocalDate.of(2024, 8, 20)
    );

    var stopWatch = new StopWatch();
    stopWatch.start();

    int _1만 = 10000;
    var posts = IntStream.range(0, _1만 * 100)
        .parallel()
        .mapToObj(i -> easyRandom.nextObject(Post.class))
        .toList();

    stopWatch.stop();

    System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

    var queryStopWatch = new StopWatch();
    queryStopWatch.start();

    postRepository.bulkInsert(posts);

    queryStopWatch.stop();

    System.out.println("DB Insert 시간 : " + queryStopWatch.getTotalTimeSeconds());
  }

}
