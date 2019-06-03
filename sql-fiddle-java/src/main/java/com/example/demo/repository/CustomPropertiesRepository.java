package com.example.demo.repository;

import com.example.demo.entity.CustomProperties;
import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPropertiesRepository extends CrudRepository<CustomProperties, Long> {

    Iterable<CustomProperties> findAllByUserAndTableQueryOrderByCreate_AtAsc(User user, TableQuery tableQuery);
//    List<CustomProperties> findAllByUserAndTableQueryOrderByCreate_AtAsc(User user, TableQuery tableQuery);
}
