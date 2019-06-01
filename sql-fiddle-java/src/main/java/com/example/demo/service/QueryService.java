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
import java.util.List;
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
                runQuery(splittedQueries, i, jdbcTemplate, userName, "INSERT INTO", 2);
            }
            if(splittedQueries[i].contains("UPDATE")) {
                runUpdateQuery(splittedQueries, i, jdbcTemplate, userName, "UPDATE", 1);
            }
            if(splittedQueries[i].contains("DELETE")) {

            }
            if(splittedQueries[i].contains("SELECT")) {

            }
        }
    }

    public void createTable(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName) throws Exception {
        int index = splittedQueries[i].indexOf("CREATE TABLE");
        splittedQueries[i] = splittedQueries[i].substring(index);

        String[] splittedCreateTableQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedCreateTableQuery, userName, 2);

        String createTableQuery = concatenateQuery(splittedCreateTableQuery, buildedTableName, 2);

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

    public String processQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        int startIndex = splittedQueries[i].indexOf(queryType);
        splittedQueries[i] = splittedQueries[i].substring(startIndex);
        String[] splittedInsertQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedInsertQuery, userName, index);

        String insertQuery = concatenateQuery(splittedInsertQuery, buildedTableName, index);

        TableQuery tableObject = tableQueryRepository.findByName(buildedTableName);

        if(tableObject == null) {
            throw new NoSuchElementException("Name of the table used in " + queryType + " query does not exists");
        }
        return insertQuery;
    }

    public void runQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String query = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);
        jdbcTemplate.execute(query);
    }

    public void runUpdateQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String query = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);

        Object[] array = computeVariables(query);

        boolean isNumberValue = false;
        int indexArray = 0;
        for(int j=0; j<query.length(); j++) {

            int indexFirstApostropheSign = 0;
            int indexSecondApostropheSign = 0;
            int indexNumber = 0;

            if(query.charAt(j) == '=') {
                indexFirstApostropheSign = j + 2;

                String substr = query.substring(indexFirstApostropheSign, indexFirstApostropheSign + 1);
                if(isNumeric(substr)) {
                    isNumberValue = true;
                }
            }

            if(isNumberValue == false) {
                if(indexFirstApostropheSign != 0) {

                    for(int k=indexFirstApostropheSign+1; k<query.length(); k++) {
                        if(indexSecondApostropheSign != 0) {
                            break;
                        }
                        if(query.charAt(k) == '\'') {
                            indexSecondApostropheSign = k;
                        }
                    }
                    String stringValue = query.substring(indexFirstApostropheSign, indexSecondApostropheSign);
                    array[indexArray] = stringValue;
                }
            } else {
                for(int l=indexFirstApostropheSign+1; l<query.length(); i++) {
                    if(query.charAt(l) == ' ') {
                        indexNumber = l
                    }
                }
            }

            
        }
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

    public String buildTableName(String[] splittedCreateTableQuery, String userName, int index) {
        String tableName = splittedCreateTableQuery[index];

        StringBuilder builder = new StringBuilder();
        builder.append(tableName).append("_").append(userName);
        return builder.toString();
    }

    public String concatenateQuery(String[] splittedCreateTableQuery, String buildedTableName, int index) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<splittedCreateTableQuery.length; i++) {
            if(i == index) {
                builder.append(buildedTableName).append(" ");
                continue;
            }
            builder.append(splittedCreateTableQuery[i]).append(" ");
        }
        return builder.toString();
    }

    public boolean isNumeric(String potentialNumber) {
        return potentialNumber.matches("-?\\d+(\\.\\d)?");
    }

    public Object[] computeVariables(String query) {
        int counter = 0;
        for(int i=0; i<query.length(); i++) {
            if(query.charAt(i) == '?') {
                counter++;
            }
        }
        return new Object[counter];
    }


}
