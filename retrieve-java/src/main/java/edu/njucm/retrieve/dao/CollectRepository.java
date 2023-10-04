package edu.njucm.retrieve.dao;

import edu.njucm.retrieve.model.Collect;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectRepository extends CrudRepository<Collect, Long> {
    List<Collect> findAllByUsername(String username);

    @Query("select count(*) from Collect where fileId=?1")
    Integer collectNum(Long fileId);
}
