package com.example.demo.service;

import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.exceptions.TableAlreadyExistsException;
import com.example.demo.repository.TableQueryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.NoSuchElementException;

@Service
public class QueryService {

    @Autowired
    private TableQueryRepository tableQueryRepository;

    @Autowired
    private UserRepository userRepository;

    public String[] decodeAndSplitUrl(String query) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(query, "UTF-8");
        String replacedString = decoded.replace("\n\t", " ").replace("\n", " ")
                .replace("=", "");

        String[] splittedDecodedQueries = replacedString.split(";");

        return splittedDecodedQueries;
    }

    public void manageQueries(String query, JdbcTemplate jdbcTemplate, String userName) throws Exception {
        String[] splittedQueries = decodeAndSplitUrl(query);

        for(int i=0; i<splittedQueries.length; i++) {
            if(splittedQueries[i].contains("CREATE TABLE")) {
                createTable(splittedQueries, i, jdbcTemplate, userName);
            }
            if(splittedQueries[i].contains("INSERT INTO")) {
                insertQuery(splittedQueries, i, jdbcTemplate, userName);
            }
            if(splittedQueries[i].contains("UPDATE")) {

            }
            if(splittedQueries[i].contains("SELECT")) {

            }
        }
    }

    public void createTable(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName) throws Exception {
        int index = splittedQueries[i].indexOf("CREATE TABLE");
        splittedQueries[i] = splittedQueries[i].substring(index);

        String[] splittedCreateTableQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedCreateTableQuery, userName);

        String createTableQuery = concatenateQuery(splittedCreateTableQuery, buildedTableName);

        TableQuery table = tableQueryRepository.findByName(buildedTableName);

        if(table != null) {
            String dropTableQUery = "DROP TABLE " + "public." + buildedTableName;
            jdbcTemplate.execute(dropTableQUery);
        } else {
            saveTable(buildedTableName, userName);
        }
        try {
            jdbcTemplate.execute(createTableQuery);
        } catch(Exception e) {
            throw new Exception("CREATE TABLE query is not correct.");
        }
    }

    public void insertQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName) {
        int index = splittedQueries[i].indexOf("INSERT INTO");
        splittedQueries[i] = splittedQueries[i].substring(index);
        String[] splittedInsertQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedInsertQuery, userName);

        String insertQuery = concatenateQuery(splittedInsertQuery, buildedTableName);

        TableQuery tableObject = tableQueryRepository.findByName(buildedTableName);

        if(tableObject == null) {
            throw new NoSuchElementException("Name of the table used in INSERT INTO query does not exists");
        }
        jdbcTemplate.update(insertQuery);
    }

    public void saveTable(String tableName, String userName) {
        TableQuery tableQueryByName = tableQueryRepository.findByName(tableName);

        if(tableQueryByName != null) {
            throw new TableAlreadyExistsException("Table name '" + tableQueryByName.getName() + "' already exists");
        }

        User user = userRepository.findByUserName(userName);

        if(user == null) {
            throw new NoSuchElementException("Given user does not exists");
        }

        TableQuery tableQuery = new TableQuery();

        tableQuery.setName(tableName);
        tableQuery.setUser(user);

        tableQueryRepository.save(tableQuery);
    }

    public String buildTableName(String[] splittedCreateTableQuery, String userName) {
        String tableName = splittedCreateTableQuery[2];

        StringBuilder builder = new StringBuilder();
        builder.append(tableName).append("_").append(userName);
        return builder.toString();
    }

    public String concatenateQuery(String[] splittedCreateTableQuery, String buildedTableName) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<splittedCreateTableQuery.length; i++) {
            if(i == 2) {
                builder.append(buildedTableName).append(" ");
                continue;
            }
            builder.append(splittedCreateTableQuery[i]).append(" ");
        }
        return builder.toString();
    }
}
