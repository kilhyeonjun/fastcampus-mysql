package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import java.sql.ResultSet;
import java.util.List;
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
public class TimelineRepository {

  private static final String TABLE = "Timeline";
  private static final RowMapper<Timeline> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> new Timeline(
      resultSet.getLong("id"),
      resultSet.getLong("memberId"),
      resultSet.getLong("postId"),
      resultSet.getTimestamp("createdAt").toLocalDateTime()
  );
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Timeline save(Timeline timeline) {
    if (timeline.getId() == null) {
      return Insert(timeline);
    }

    throw new UnsupportedOperationException("Timeline은 갱신을 지원하지 않습니다.");
  }

  public void bulkInsert(List<Timeline> timelines) {
    var sql = String.format("""
        INSERT INTO %s (memberId, postId, createdAt)
        VALUES (:memberId, :postId, :createdAt)
        """, TABLE);

    var params = timelines.stream()
        .map(BeanPropertySqlParameterSource::new)
        .toArray(SqlParameterSource[]::new);

    namedParameterJdbcTemplate.batchUpdate(sql, params);
  }


  public List<Timeline> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
    var sql = String.format("""
        SELECT *
        FROM %s
        WHERE memberId = :memberId
        ORDER BY id DESC
        LIMIT :size
        """, TABLE);

    var params = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("size", size);

    return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
  }

  public List<Timeline> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId,
      int size) {
    var sql = String.format("""
        SELECT *
        FROM %s
        WHERE memberId = :memberId AND id < :id
        ORDER BY id DESC
        LIMIT :size
        """, TABLE);

    var params = new MapSqlParameterSource()
        .addValue("id", id)
        .addValue("memberId", memberId)
        .addValue("size", size);

    return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
  }

  private Timeline Insert(Timeline timeline) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
        namedParameterJdbcTemplate.getJdbcTemplate())
        .withTableName(TABLE)
        .usingGeneratedKeyColumns("id");

    SqlParameterSource params = new BeanPropertySqlParameterSource(timeline);
    var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

    return Timeline.builder()
        .id(id)
        .memberId(timeline.getMemberId())
        .postId(timeline.getPostId())
        .createdAt(timeline.getCreatedAt())
        .build();
  }

}
