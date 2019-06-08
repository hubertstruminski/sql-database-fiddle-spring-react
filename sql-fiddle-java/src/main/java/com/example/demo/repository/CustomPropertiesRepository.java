package com.example.demo.repository;

import com.example.demo.entity.CustomInsert;
import com.example.demo.entity.CustomProperties;
import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface CustomPropertiesRepository extends CrudRepository<CustomProperties, Long> {

    List<CustomProperties> findAllByUserAndTableQueryOrderByCreateAtAsc(User user, TableQuery tableQuery);

    CustomProperties findFirstByValue(String value);

    List<CustomProperties> findAllByCustomInsert(CustomInsert customInsert);

    @Modifying
    @Transactional
    void deleteByCustomInsert(CustomInsert customInsert);
}
