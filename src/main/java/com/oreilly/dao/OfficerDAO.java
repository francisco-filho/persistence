package com.oreilly.dao;

import com.oreilly.entities.Officer;

import java.util.Collection;

public interface OfficerDAO {
  Officer findOne(Integer id);
  Collection<Officer> findAll();
  Officer save(Officer officer);
  void delete(Officer officer);
  boolean exists(Integer id);
  Long count();
}
