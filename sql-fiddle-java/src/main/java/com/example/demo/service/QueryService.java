package com.example.demo.service;

import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.repository.TableQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Service
public class QueryService {

    @Autowired
    private TableQueryRepository tableQueryRepository;

    public String[] decodeAndSplitUrl(String query) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(query, "UTF-8");
        String replacedString = decoded.replace("\n\t", " ").replace("\n", " ")
                .replace("=", "");

        String[] splittedDecodedQueries = replacedString.split(";");

        return splittedDecodedQueries;
    }

    public void manageQueries(String query, JdbcTemplate jdbcTemplate, User user) throws UnsupportedEncodingException {
        String[] splittedQueries = decodeAndSplitUrl(query);

        for(int i=0; i<splittedQueries.length; i++) {
            if(splittedQueries[i].contains("CREATE TABLE")) {
                String[] splittedCreateTableQuery = splittedQueries[i].split(" ");
                String tableName = splittedCreateTableQuery[2];

                TableQuery tableQuery = saveTable(tableName, user);
            }
            if(splittedQueries[i].contains("INSERT INTO")) {
                jdbcTemplate.update(splittedQueries[i]);
            }
            if(splittedQueries[i].contains("UPDATE")) {

            }
            if(splittedQueries[i].contains("SELECT")) {

            }
        }
    }

    public TableQuery saveTable(String tableName, User user) {
        TableQuery tableQuery = new TableQuery();

        tableQuery.setName(tableName);
        tableQuery.setUser(user);

        return tableQueryRepository.save(tableQuery);
    }
}
