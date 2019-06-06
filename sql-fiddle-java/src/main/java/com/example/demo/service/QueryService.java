package com.example.demo.service;

import com.example.demo.entity.CustomInsert;
import com.example.demo.entity.CustomProperties;
import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.exceptions.TableAlreadyExistsException;
import com.example.demo.repository.CustomPropertiesRepository;
import com.example.demo.repository.TableQueryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.Date;

@Service
public class QueryService {

    @Autowired
    private TableQueryRepository tableQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPropertiesRepository customPropertiesRepository;

    public String[] decodeAndSplitUrl(String query) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(query, "UTF-8");
        String replacedString = decoded.replace("\n\t", " ").replace("\n", " ")
                .replace("=", "");

        String[] splittedDecodedQueries = replacedString.split(";");

        return splittedDecodedQueries;
    }

    public String[] decodeAndSplitUrlForUpdateQuery(String query) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(query, "UTF-8");
        String replacedString = decoded.replace("\n\t", " ").replace("\n", " ");

        String[] splittedDecodedQueries = replacedString.split(";");

        for(int i=0; i<splittedDecodedQueries.length; i++) {
            if(splittedDecodedQueries[i].contains("UPDATE")) {
                splittedDecodedQueries[i] = splittedDecodedQueries[i].substring(0, splittedDecodedQueries[i].length());
                break;
            }
        }

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
                String[] splittedUpdateQueries = decodeAndSplitUrlForUpdateQuery(query);
                for(int j=0; j<splittedUpdateQueries.length; j++) {
                    if(splittedUpdateQueries[j].contains("UPDATE")) {
                        runUpdateQuery(splittedUpdateQueries, j, jdbcTemplate, userName, "UPDATE", 1);
                    }
                }
            }
            if(splittedQueries[i].contains("DELETE")) {
                String[] splittedDeleteQueries = decodeAndSplitUrlForUpdateQuery(query);
                for(int j=0; j<splittedDeleteQueries.length; j++) {
                    if(splittedDeleteQueries[j].contains("DELETE")) {
                        runUpdateQuery(splittedDeleteQueries, j, jdbcTemplate, userName, "DELETE", 2);
                    }
                }
            }
        }
    }

    public void createTable(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName) throws Exception {
        int index = splittedQueries[i].indexOf("CREATE TABLE");
        splittedQueries[i] = splittedQueries[i].substring(index);

        String[] splittedCreateTableQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedCreateTableQuery, userName, 2);

        String tableNameBeforeBuild = splittedCreateTableQuery[2];
        String createTableQuery = concatenateQuery(splittedCreateTableQuery, buildedTableName, 2);

        TableQuery table = tableQueryRepository.findByBuildedName(buildedTableName);

        if(table != null) {
            String dropTableQUery = "DROP TABLE " + "public." + buildedTableName;
            jdbcTemplate.execute(dropTableQUery);
        } else {
            saveTable(buildedTableName, userName, tableNameBeforeBuild, createTableQuery);
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

        TableQuery tableObject = tableQueryRepository.findByBuildedName(buildedTableName);

        if(tableObject == null) {
            throw new NoSuchElementException("Name of the table used in " + queryType + " query does not exists");
        }
        return insertQuery;
    }

    public void runQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String insertQuery = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);

        User userByUserName = userRepository.findByUserName(userName);

        String[] fields = getFieldsFromInsertQuery(insertQuery);
        String[] values = getValuesFromInsertQuery(insertQuery);
        String buildedTableName = getBuildedTableName(insertQuery);

        saveCustomProperties(buildedTableName, insertQuery, fields, values, userByUserName);
        jdbcTemplate.execute(insertQuery);
    }

    public void runUpdateQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String query = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);

        int setIndex = query.indexOf("SET");
        int whereIndex = query.indexOf("WHERE");
        String subQuery = query.substring(setIndex + 4, whereIndex - 1);
        String[] splitSubQuery =subQuery.trim().split(",");

        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for(int j=0; j<splitSubQuery.length; j++) {
            if(splitSubQuery[j].length() < 2) {
                continue;
            }
            int indexSplitSubQuery = splitSubQuery[j].indexOf("=");
            String field = splitSubQuery[j].substring(0, indexSplitSubQuery);
            String value = splitSubQuery[j].substring(indexSplitSubQuery + 1);
            fields.add(field);
            values.add(value);
        }



        int startIndex = query.trim().indexOf("id=");
        String id = query.substring(startIndex + 3);

        CustomProperties customProperties = customPropertiesRepository.findFirstByValue(id);
        CustomInsert customInsert = customProperties.getCustomInsert();

        List<CustomProperties> customPropertiesList = customPropertiesRepository.findAllByCustomInsert(customInsert);

        for(int j=0; j<customPropertiesList.size(); j++) {
            for(int k=0; k<fields.size(); k++) {
                if(customPropertiesList.get(j).getField().equals(fields.get(k))) {
                    customPropertiesList.get(j).setValue(values.get(k));
                    customPropertiesRepository.save(customPropertiesList.get(j));
                }
            }

        }


        Object[] array = algorithmForUpdateQuery(query);
        parseNumbers(array);

        String updateQueryResult = changeValuesOnQuestionMark(query);

        jdbcTemplate.update(updateQueryResult, array);
    }


    public void saveTable(String tableName, String userName, String tableNameBefore, String createTableQuery) {
        TableQuery tableQueryByName = tableQueryRepository.findByBuildedName(tableName);

        if(tableQueryByName != null) {
            throw new TableAlreadyExistsException("Table name '" + tableQueryByName.getTableNameBefore() + "' already exists");
        }

        User user = userRepository.findByUserName(userName);

        if(user == null) {
            throw new NoSuchElementException("Given user does not exists");
        }

        List<String> result = processColumnsFromCreateQuery(createTableQuery);

        TableQuery tableQuery = saveTableQuery(tableName, user, tableNameBefore, createTableQuery, result);
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
            if(query.charAt(i) == '=') {
                counter++;
            }
        }
        return new Object[counter];
    }

    public Object[] algorithmForUpdateQuery(String query) {
        Object[] array = computeVariables(query);

        boolean isNumberValue = false;
        int indexArray = 0;
        for(int j=0; j<query.length(); j++) {

            int indexFirstApostropheSign = 0;
            int indexSecondApostropheSign = 0;
            int indexNumber = 0;

            if(query.charAt(j) == '=') {
                indexFirstApostropheSign = j + 2;

                String potentialNumber = String.valueOf(query.charAt(indexFirstApostropheSign));
                if(isNumeric(potentialNumber)) {
                    isNumberValue = true;
                } else {
                    String substr = query.substring(indexFirstApostropheSign+1, indexFirstApostropheSign + 2);
                }
            }

            if(isNumberValue == false) {
                if(indexFirstApostropheSign != 0) {

                    for(int k=indexFirstApostropheSign+1; k<query.length(); k++) {
                        if(query.charAt(k) == '\'') {
                            indexSecondApostropheSign = k;
                            break;
                        }
                    }
                    String stringValue = query.substring(indexFirstApostropheSign + 1, indexSecondApostropheSign);
                    array[indexArray] = stringValue;
                    indexArray++;
                }
            } else {
                for(int l=indexFirstApostropheSign; l<query.length(); l++) {
                    if(query.charAt(l) == ' ') {
                        indexNumber = l;
                        isNumberValue = false;
                        break;
                    }
                }
                String stringValue = query.substring(indexFirstApostropheSign, indexNumber);
                array[indexArray] = stringValue;
                indexArray++;
            }
        }
        return array;
    }

    public void parseNumbers(Object[] array) {
        for(int a=0; a<array.length; a++) {
            if(isNumeric(String.valueOf(array[a]))) {
                if(String.valueOf(array[a]).contains(".")) {
                    array[a] = Double.parseDouble(String.valueOf(array[a]));
                    continue;
                }
                array[a] = Integer.parseInt(String.valueOf(array[a]));
            }
        }
    }

    public String changeValuesOnQuestionMark(String query) {
        String[] splitUpdateQuery = query.split(" ");

        for(int m=0; m<splitUpdateQuery.length; m++) {
            if(splitUpdateQuery[m].equals("=")) {
                splitUpdateQuery[m + 1] = "?";
            }
        }
        return concatenateQuery(splitUpdateQuery, splitUpdateQuery[1], 1);
    }

    public TableQuery findById(Long id, JdbcTemplate jdbcTemplate, String insertQuery, String userName) {
        TableQuery firstById = tableQueryRepository.findFirstById(id);

        if(firstById == null) {
            throw new NoSuchElementException("Given table does not exists.");
        }



        return firstById;
    }

    private boolean isWhiteSpace(String word) {
        return word.matches("\\s+");
    }

    private String[] getFieldsFromInsertQuery(String insertQuery) {
        int indexParenthesis = insertQuery.indexOf("(");
        int indexSecondParenthesis = insertQuery.indexOf(")");
        String subCreateQuery = insertQuery.substring(indexParenthesis + 1, indexSecondParenthesis);
        return subCreateQuery.trim().split(",");
    }

    private String[] getValuesFromInsertQuery(String insertQuery) {
        int indexAfterValues = insertQuery.indexOf("VALUES ("); // +8
        String substr = insertQuery.substring(indexAfterValues);
        int indexAtEnd = substr.indexOf(")");
        String subInsertQuery = substr.substring(8, indexAtEnd);
        String[] values = subInsertQuery.trim().split(",");

        for(int a=0; a<values.length; a++) {
            values[a] = values[a].replace("\'", "").replace(" ", "");
        }
        return values;
    }

    private String getBuildedTableName(String insertQuery) {
        String[] splitInsertQuery = insertQuery.split(" ");
        return splitInsertQuery[2];
    }

    private void saveCustomProperties(String buildedTableName, String insertQuery, String[] fields, String[] values,
                                      User userByUserName) {
        TableQuery tableQuery = tableQueryRepository.findByBuildedName(buildedTableName);

        CustomInsert customInsert = new CustomInsert();
        customInsert.setInsertQuery(insertQuery);

        for(int a=0; a<fields.length; a++) {
            CustomProperties customProperties = new CustomProperties();

            customProperties.setField(fields[a]);
            customProperties.setValue(values[a]);
            customProperties.setUser(userByUserName);
            customProperties.setCustomInsert(customInsert);
            customProperties.setCreateAt(new Date());
            customProperties.setTableQuery(tableQuery);

            customPropertiesRepository.save(customProperties);
        }
    }

    private List<String> processColumnsFromCreateQuery(String createTableQuery) {
        String[] splitSubCreateQuery = splitCreateQueryToColumns(createTableQuery);

        List<String> result = new ArrayList<>();
        int firstIndex =  0;

        for(int i=0; i<splitSubCreateQuery.length; i++) {
            if(isWhiteSpace(splitSubCreateQuery[i]) || splitSubCreateQuery[i].length() == 0) {
                continue;
            }
            if(isWhiteSpace(String.valueOf(splitSubCreateQuery[i].charAt(0)))) {
                firstIndex = splitSubCreateQuery[i].indexOf(splitSubCreateQuery[i].charAt(1));
            } else {
                firstIndex = splitSubCreateQuery[i].indexOf(splitSubCreateQuery[i].charAt(0));
            }
            int secondIndex = splitSubCreateQuery[i].substring(firstIndex).indexOf(" ");
            splitSubCreateQuery[i] = splitSubCreateQuery[i].substring(firstIndex, secondIndex + 1);

            result.add(splitSubCreateQuery[i]);
        }
        return result;
    }

    private String[] splitCreateQueryToColumns(String createTableQuery) {
        int indexParenthesis = createTableQuery.indexOf("(");
        int indexPrimary = createTableQuery.indexOf("PRIMARY");
        String subCreateQuery = createTableQuery.substring(indexParenthesis + 1, indexPrimary);
        return subCreateQuery.split(",");
    }

    private TableQuery saveTableQuery(String tableName, User user, String tableNameBefore, String createTableQuery,
                                List<String> result) {
        TableQuery tableQuery = new TableQuery();

        tableQuery.setBuildedName(tableName);
        tableQuery.setUser(user);
        tableQuery.setTableNameBefore(tableNameBefore);
        tableQuery.setCreateQuery(createTableQuery);

        String selectQuery = "SELECT * FROM " + tableName;
        tableQuery.setSelectQuery(selectQuery);

        // amount of columns
        tableQuery.setAmountColumns(result.size());

        return tableQuery;
    }
}
