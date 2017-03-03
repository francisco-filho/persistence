package com.oreilly.dao;

import com.oreilly.DatabaseConfiguration;
import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@ContextConfiguration(classes = DatabaseConfiguration.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class OfficeRepositoryTest {
  @Autowired
  private OfficerRepository dao;

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
    assertThat(0L, is(dao.count()));
  }

  @Test
  public void exists() throws Exception {
    IntStream.rangeClosed(1, 5)
        .forEach(i -> assertTrue(dao.exists(i)));
  }

  @Test
  public void count() throws Exception {
    assertThat(5L, is(dao.count()));
  }

}