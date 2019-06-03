package com.example.demo.repository;

import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableQueryRepository extends CrudRepository<TableQuery, Long> {

    TableQuery findByBuildedName(String buildedName);

    Iterable<TableQuery> findAllByUser(User user);

    TableQuery findByIdAndUser(Long id, User user);

    TableQuery findFirstById(Long id);

    int findFirstByBuildedName(String buildedTableName);
}
