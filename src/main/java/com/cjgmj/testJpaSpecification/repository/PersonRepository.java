package com.cjgmj.testJpaSpecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjgmj.testJpaSpecification.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
