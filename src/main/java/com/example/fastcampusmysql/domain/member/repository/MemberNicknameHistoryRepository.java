package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import java.sql.ResultSet;
import java.time.LocalDateTime;
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
public class MemberNicknameHistoryRepository {

  static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet resultSet, int rowNum) -> MemberNicknameHistory.builder()
      .id(resultSet.getLong("id"))
      .memberId(resultSet.getLong("memberId"))
      .nickname(resultSet.getString("nickname"))
      .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
      .build();

  private static final String TABLE = "MemberNicknameHistory";
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<MemberNicknameHistory> findAllByMemberId(Long memberId) {
    var sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
    var params = new MapSqlParameterSource().addValue("memberId", memberId);
    return namedParameterJdbcTemplate.query(sql, params, rowMapper);
  }

  public MemberNicknameHistory save(MemberNicknameHistory member) {
    if (member.getId() == null) {
      return insert(member);
    }

    throw new UnsupportedOperationException("MemberNicknameHistory는 갱신을 지원하지 않습니다.");
  }

  private MemberNicknameHistory insert(MemberNicknameHistory member) {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
        namedParameterJdbcTemplate.getJdbcTemplate())
        .withTableName(TABLE)
        .usingGeneratedKeyColumns("id");
    SqlParameterSource params = new BeanPropertySqlParameterSource(member);
    var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

    return MemberNicknameHistory.builder()
        .id(id)
        .memberId(member.getMemberId())
        .nickname(member.getNickname())
        .createdAt(member.getCreatedAt())
        .build();
  }

}
