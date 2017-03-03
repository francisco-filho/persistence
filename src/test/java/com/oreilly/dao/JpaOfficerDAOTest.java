package com.oreilly.dao;

import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JpaOfficerDAOTest {
  @Autowired @Qualifier("jpaOfficerDAO")
  private OfficerDAO dao;

  @Test
  public void findOne() throws Exception {
    Officer one = dao.findOne(1);
    assertThat("Kirk", is(one.getLast()));
  }

  @Test
  public void findAll() throws Exception {
    Collection<Officer> officers = dao.findAll();

    List<String> lastNames = officers.stream()
        .map(Officer::getLast)
        .collect(Collectors.toList());

    assertThat(5, is(officers.size()));
    assertThat(lastNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
  }

  @Test
  public void save() throws Exception {
    Officer officer = dao.save(new Officer(Rank.COMMANDER, "first", "last"));
    assertNotNull(officer.getId());
  }

  @Test
  public void delete() throws Exception {
    IntStream.rangeClosed(1, 5)
        .forEach(i -> dao.delete(dao.findOne(i)));
    assertThat(0, is(dao.count().intValue()));
  }

  @Test
  public void exists() throws Exception {
    IntStream.rangeClosed(1, 5)
        .forEach(i -> assertTrue(dao.exists(i)));
  }

  @Test
  public void count() throws Exception {
    assertThat(5, is(dao.count().intValue()));
  }

}