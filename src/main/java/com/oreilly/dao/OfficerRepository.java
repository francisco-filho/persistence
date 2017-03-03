package com.oreilly.dao;

import com.oreilly.entities.Officer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficerRepository extends JpaRepository<Officer, Integer>{}
