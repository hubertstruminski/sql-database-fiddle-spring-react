package com.example.demo.repository;

import com.example.demo.entity.CustomInsert;
import com.example.demo.entity.CustomProperties;
import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPropertiesRepository extends CrudRepository<CustomProperties, Long> {

    List<CustomProperties> findAllByUserAndTableQueryOrderByCreateAtAsc(User user, TableQuery tableQuery);

    CustomProperties findFirstByValue(String value);

    List<CustomProperties> findAllByCustomInsert(CustomInsert customInsert);


}
