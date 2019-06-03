package com.example.demo.service;

import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.repository.TableQueryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TableQueryService {

    @Autowired
    private TableQueryRepository tableQueryRepository;

    @Autowired
    private UserRepository userRepository;

    public Iterable<TableQuery> findAllButtons(String userName) {
        User foundUser = userRepository.findByUserName(userName);

        if(foundUser == null) {
            throw new NoSuchElementException("User '" + userName + "' does not exists.");
        }
        return tableQueryRepository.findAllByUser(foundUser);
    }

    public TableQuery findByIdAndUserName(Long id, String userName) {
        User foundUser = userRepository.findByUserName(userName);

        if(foundUser == null) {
            throw new NoSuchElementException("User '" + userName + "' does not exists.");
        }
        return tableQueryRepository.findByIdAndUser(id, foundUser);
    }
}
