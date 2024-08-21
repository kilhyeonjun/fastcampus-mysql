package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.PostLike;
import java.sql.ResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostLikeRepository {

  private static final String TABLE = "PostLike";
  private static final RowMapper<PostLike> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> new PostLike(
      resultSet.getLong("id"),
      resultSet.getLong("memberId"),
      resultSet.getLong("postId"),
      resultSet.getTimestamp("createdAt").toLocalDateTime()
  );
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Long count(Long postId) {
    var sql = String.format("""
        SELECT COUNT(id)
        FROM %s
        WHERE postId = :postId
        """, TABLE);

    var params = new MapSqlParameterSource().addValue("postId", postId);

    return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
  }

  public PostLike save(PostLike postLike) {
    if (postLike.getId() == null) {
      return Insert(postLike);
    }

    throw new UnsupportedOperationException("PostLike은 갱신을 지원하지 않습니다.");
  }


  private PostLike Insert(PostLike postLike) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
        namedParameterJdbcTemplate.getJdbcTemplate())
        .withTableName(TABLE)
        .usingGeneratedKeyColumns("id");

    SqlParameterSource params = new BeanPropertySqlParameterSource(postLike);
    var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

    return PostLike.builder()
        .id(id)
        .memberId(postLike.getMemberId())
        .postId(postLike.getPostId())
        .createdAt(postLike.getCreatedAt())
        .build();
  }

}
