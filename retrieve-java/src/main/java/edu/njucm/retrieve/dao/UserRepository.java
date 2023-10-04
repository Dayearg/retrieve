package edu.njucm.retrieve.dao;

import edu.njucm.retrieve.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}