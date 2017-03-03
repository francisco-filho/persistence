package com.oreilly.dao;

import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class JdbcOfficerDAO implements OfficerDAO {
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert insert;

  public JdbcOfficerDAO(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
    insert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("officers")
        .usingGeneratedKeyColumns("id");
  }

  @Override
  public Officer findOne(Integer id) {
    return jdbcTemplate.queryForObject("SELECT * FROM officers WHERE id = ?", (rs, rowNum) -> {
      return new Officer(
          rs.getInt("id"),
          Rank.valueOf(rs.getString("rank")),
          rs.getString("first_name"),
          rs.getString("last_name"));
    }, id);
  }

  @Override
  public Collection<Officer> findAll() {
    return jdbcTemplate.query("SELECT * FROM officers", (rs, rowNum) -> {
      return new Officer(
          rs.getInt("id"),
          Rank.valueOf(rs.getString("rank")),
          rs.getString("first_name"),
          rs.getString("last_name"));
    });
  }

  @Override
  public Officer save(Officer officer) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("first_name", officer.getFirst())
        .addValue("last_name", officer.getLast())
        .addValue("rank", officer.getRank());

    Integer id = (Integer) insert.executeAndReturnKey(params);
    officer.setId(id);

    return officer;
  }

  @Override
  public void delete(Officer officer) {
    jdbcTemplate.update("DELETE FROM officers WHERE id = ?", officer.getId());
  }

  @Override
  public boolean exists(Integer id) {
    return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM officers WHERE id = ?)", Boolean.class, id);
  }

  @Override
  public Long count() {
    return jdbcTemplate.queryForObject("SELECT count(*) FROM officers;", Long.class);
  }
}
