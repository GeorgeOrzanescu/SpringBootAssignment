package com.assignment.assignment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends CrudRepository<Link, Long> {
}
