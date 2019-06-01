package com.example.demo.repository;

import com.example.demo.entity.TableQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableQueryRepository extends CrudRepository<TableQuery, Long> {

    TableQuery findByName(String name);
}
