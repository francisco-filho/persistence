package com.oreilly.dao;

import com.oreilly.entities.Officer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository
public class JpaOfficerDAO implements OfficerDAO {
  @PersistenceContext
  EntityManager entityManager;

   @Override
  public Officer findOne(Integer id) {
    return entityManager.find(Officer.class, id);
  }

  @Override
  public Collection<Officer> findAll() {
    return entityManager.createQuery("select o from Officer o").getResultList();
  }

  @Override
  public Officer save(Officer officer) {
    entityManager.persist(officer);
    return officer;
  }

  @Override
  public void delete(Officer officer) {
    entityManager.remove(officer);
  }

  @Override
  public boolean exists(Integer id) {
    return entityManager.find(Officer.class, id) != null;
  }

  @Override
  public Long count() {
    return (Long) entityManager.createQuery("select count(o.id) from Officer o").getSingleResult();
  }
}
