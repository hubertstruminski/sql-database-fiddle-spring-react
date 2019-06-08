package com.example.demo.service;

import com.example.demo.entity.CustomInsert;
import com.example.demo.entity.CustomProperties;
import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.exceptions.TableAlreadyExistsException;
import com.example.demo.repository.CustomInsertRepository;
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

    @Autowired
    private CustomInsertRepository customInsertRepository;

    private String[] decodeAndSplitUrl(String query) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(query, "UTF-8");
        String replacedString = decoded.replace("\n\t", " ").replace("\n", " ")
                .replace("=", "");

        return replacedString.split(";");
    }

    private String[] decodeAndSplitUrlForUpdateQuery(String query) throws UnsupportedEncodingException {
        String decoded = URLDecoder.decode(query, "UTF-8");
        String replacedString = decoded.replace("\n\t", " ").replace("\n", " ");

        String[] splittedDecodedQueries = replacedString.split(";");

        for (int i = 0; i < splittedDecodedQueries.length; i++) {
            if (splittedDecodedQueries[i].contains("UPDATE")) {
                splittedDecodedQueries[i] = splittedDecodedQueries[i].substring(0, splittedDecodedQueries[i].length());
                break;
            }
        }
        return splittedDecodedQueries;
    }

    public void manageQueries(String query, JdbcTemplate jdbcTemplate, String userName) throws Exception {
        String[] splittedQueries = decodeAndSplitUrl(query);

        for (int i = 0; i < splittedQueries.length; i++) {
            if (splittedQueries[i].contains("CREATE TABLE")) {
                createTable(splittedQueries, i, jdbcTemplate, userName);
            }
            if (splittedQueries[i].contains("INSERT INTO")) {
                runQuery(splittedQueries, i, jdbcTemplate, userName, "INSERT INTO", 2);
            }
            if (splittedQueries[i].contains("UPDATE")) {
                String[] splittedUpdateQueries = decodeAndSplitUrlForUpdateQuery(query);
                for (int j = 0; j < splittedUpdateQueries.length; j++) {
                    if (splittedUpdateQueries[j].contains("UPDATE")) {
                        updateQuery(splittedUpdateQueries, j, jdbcTemplate, userName, "UPDATE", 1);
                    }
                }
            }
            if (splittedQueries[i].contains("DELETE")) {
                String[] splittedDeleteQueries = decodeAndSplitUrlForUpdateQuery(query);
                for (int j = 0; j < splittedDeleteQueries.length; j++) {
                    if (splittedDeleteQueries[j].contains("DELETE")) {
                        deleteQuery(splittedDeleteQueries, j, jdbcTemplate, userName, "DELETE", 2);
                    }
                }
            }
        }
    }

    private void createTable(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName) throws Exception {
        int index = splittedQueries[i].indexOf("CREATE TABLE");
        splittedQueries[i] = splittedQueries[i].substring(index);

        String[] splittedCreateTableQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedCreateTableQuery, userName, 2);

        String tableNameBeforeBuild = splittedCreateTableQuery[2];
        String createTableQuery = concatenateQuery(splittedCreateTableQuery, buildedTableName, 2);

        TableQuery table = tableQueryRepository.findByBuildedName(buildedTableName);

        if (table != null) {
            String dropTableQUery = "DROP TABLE " + "public." + buildedTableName;
            jdbcTemplate.execute(dropTableQUery);
        } else {
            saveTable(buildedTableName, userName, tableNameBeforeBuild, createTableQuery);
        }
        try {
            jdbcTemplate.execute(createTableQuery);
        } catch (Exception e) {
            throw new Exception("CREATE TABLE query is not correct.");
        }
    }

    private String processQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        int startIndex = splittedQueries[i].indexOf(queryType);
        splittedQueries[i] = splittedQueries[i].substring(startIndex);
        String[] splittedInsertQuery = splittedQueries[i].split(" ");
        String buildedTableName = buildTableName(splittedInsertQuery, userName, index);

        String insertQuery = concatenateQuery(splittedInsertQuery, buildedTableName, index);

        TableQuery tableObject = tableQueryRepository.findByBuildedName(buildedTableName);

        if (tableObject == null) {
            throw new NoSuchElementException("Name of the table used in " + queryType + " query does not exists");
        }
        return insertQuery;
    }

    private void runQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String insertQuery = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);

        User userByUserName = userRepository.findByUserName(userName);

        String[] fields = getFieldsFromInsertQuery(insertQuery);
        String[] values = getValuesFromInsertQuery(insertQuery);
        String buildedTableName = getBuildedTableName(insertQuery);

        saveCustomProperties(buildedTableName, insertQuery, fields, values, userByUserName);
        jdbcTemplate.execute(insertQuery);
    }

    private void updateQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String query = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);
        String[] splitSubQuery = splitQueryToColumns(query, "SET", "WHERE", 4, -1);

        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int j = 0; j < splitSubQuery.length; j++) {
            if (splitSubQuery[j].length() < 2) {
                continue;
            }
            int indexSplitSubQuery = splitSubQuery[j].indexOf("=");
            String field = splitSubQuery[j].substring(0, indexSplitSubQuery);
            String value = splitSubQuery[j].substring(indexSplitSubQuery + 1);
            field = field.replace(" ", "");
            value = value.replace(" ", "").replace("\'", "");
            fields.add(field);
            values.add(value);
        }

        updateCustomProperties(query, fields, values);

        Object[] array = algorithmForUpdateQuery(query);
        parseNumbers(array);
        String[] splitUpdateQuery = changeValuesOnQuestionMark(query);
        String updateQueryResult = concatenateQuery(splitUpdateQuery, splitUpdateQuery[1], 1);

        jdbcTemplate.update(updateQueryResult, array);
    }

    private void deleteQuery(String[] splittedQueries, int i, JdbcTemplate jdbcTemplate, String userName, String queryType, int index) {
        String query = processQuery(splittedQueries, i, jdbcTemplate, userName, queryType, index);

        String id = getWhereClauseId(query);
        Long deleteId = Long.parseLong(id);

        deleteEntities(id);

        String[] splitDeleteQuery = changeValuesOnQuestionMark(query);
        String deleteQuery = concatenateQuery(splitDeleteQuery, splitDeleteQuery[2], 2);

        Object[] params = new Object[]{deleteId};
        jdbcTemplate.update(deleteQuery, params);
    }

    public void saveTable(String tableName, String userName, String tableNameBefore, String createTableQuery) {
        TableQuery tableQueryByName = tableQueryRepository.findByBuildedName(tableName);

        if (tableQueryByName != null) {
            throw new TableAlreadyExistsException("Table name '" + tableQueryByName.getTableNameBefore() + "' already exists");
        }

        User user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new NoSuchElementException("Given user does not exists");
        }

        List<String> result = processColumnsFromCreateQuery(createTableQuery);

        TableQuery tableQuery = saveTableQuery(tableName, user, tableNameBefore, createTableQuery, result);
        tableQueryRepository.save(tableQuery);
    }

    private String buildTableName(String[] splittedCreateTableQuery, String userName, int index) {
        String tableName = splittedCreateTableQuery[index];

        StringBuilder builder = new StringBuilder();
        builder.append(tableName).append("_").append(userName);
        return builder.toString();
    }

    private String concatenateQuery(String[] splittedCreateTableQuery, String buildedTableName, int index) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < splittedCreateTableQuery.length; i++) {
            if (i == index) {
                builder.append(buildedTableName).append(" ");
                continue;
            }
            builder.append(splittedCreateTableQuery[i]).append(" ");
        }
        return builder.toString();
    }

    private boolean isNumeric(String potentialNumber) {
        return potentialNumber.matches("-?\\d+(\\.\\d)?");
    }

    private Object[] computeVariables(String query) {
        int counter = 0;
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == '=') {
                counter++;
            }
        }
        return new Object[counter];
    }

    private Object[] algorithmForUpdateQuery(String query) {
        Object[] array = computeVariables(query);

        boolean isNumberValue = false;
        int indexArray = 0;
        for (int j = 0; j < query.length(); j++) {

            int indexFirstApostropheSign = 0;
            int indexSecondApostropheSign = 0;
            int indexNumber = 0;

            if (query.charAt(j) == '=') {
                indexFirstApostropheSign = j + 2;

                String potentialNumber = String.valueOf(query.charAt(indexFirstApostropheSign));
                if (isNumeric(potentialNumber)) {
                    isNumberValue = true;
                } else {
                    String substr = query.substring(indexFirstApostropheSign + 1, indexFirstApostropheSign + 2);
                }
            }

            if (!isNumberValue) {
                if (indexFirstApostropheSign != 0) {

                    for (int k = indexFirstApostropheSign + 1; k < query.length(); k++) {
                        if (query.charAt(k) == '\'') {
                            indexSecondApostropheSign = k;
                            break;
                        }
                    }
                    String stringValue = query.substring(indexFirstApostropheSign + 1, indexSecondApostropheSign);
                    array[indexArray] = stringValue;
                    indexArray++;
                }
            } else {
                for (int l = indexFirstApostropheSign; l < query.length(); l++) {
                    if (query.charAt(l) == ' ') {
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

    private void parseNumbers(Object[] array) {
        for (int a = 0; a < array.length; a++) {
            if (isNumeric(String.valueOf(array[a]))) {
                if (String.valueOf(array[a]).contains(".")) {
                    array[a] = Double.parseDouble(String.valueOf(array[a]));
                    continue;
                }
                array[a] = Integer.parseInt(String.valueOf(array[a]));
            }
        }
    }

    private String[] changeValuesOnQuestionMark(String query) {
        String[] splitUpdateQuery = query.split(" ");

        for (int m = 0; m < splitUpdateQuery.length; m++) {
            if (splitUpdateQuery[m].equals("=")) {
                splitUpdateQuery[m + 1] = "?";
            }
        }
        return splitUpdateQuery;
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

        for (int a = 0; a < values.length; a++) {
            values[a] = values[a].replace("\'", "").replace(" ", "");
        }
        return values;
    }

    private String getBuildedTableName(String insertQuery) {
        String[] splitInsertQuery = insertQuery.split(" ");
        return splitInsertQuery[2];
    }

    public void saveCustomProperties(String buildedTableName, String insertQuery, String[] fields, String[] values,
                                     User userByUserName) {
        TableQuery tableQuery = tableQueryRepository.findByBuildedName(buildedTableName);

        CustomInsert customInsert = new CustomInsert();
        customInsert.setInsertQuery(insertQuery);

        for (int a = 0; a < fields.length; a++) {
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
        String[] splitSubCreateQuery = splitQueryToColumns(createTableQuery, "(", "PRIMARY",
                1, 0);

        List<String> result = new ArrayList<>();
        int firstIndex = 0;

        for (int i = 0; i < splitSubCreateQuery.length; i++) {
            if (isWhiteSpace(splitSubCreateQuery[i]) || splitSubCreateQuery[i].length() == 0) {
                continue;
            }
            if (isWhiteSpace(String.valueOf(splitSubCreateQuery[i].charAt(0)))) {
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

    private String[] splitQueryToColumns(String createTableQuery, String firstSign, String secondSign, int firstNumber,
                                         int secondNumber) {
        int indexParenthesis = createTableQuery.indexOf(firstSign);
        int indexPrimary = createTableQuery.indexOf(secondSign);
        String subCreateQuery = createTableQuery.substring(indexParenthesis + firstNumber, indexPrimary + secondNumber);
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

    public void updateCustomProperties(String query, List<String> fields, List<String> values) {
        String id = getWhereClauseId(query);

        CustomProperties customProperties = customPropertiesRepository.findFirstByValue(id);
        CustomInsert customInsert = customProperties.getCustomInsert();

        List<CustomProperties> customPropertiesList = customPropertiesRepository.findAllByCustomInsert(customInsert);

        for (int j = 0; j < customPropertiesList.size(); j++) {
            for (int k = 0; k < fields.size(); k++) {
                if (customPropertiesList.get(j).getField().contains(fields.get(k))) {
                    customPropertiesList.get(j).setValue(values.get(k));
                    customPropertiesRepository.save(customPropertiesList.get(j));
                }
            }
        }
    }

    private String getWhereClauseId(String query) {
        int startIndex = query.trim().indexOf("id = ");
        String id = query.substring(startIndex + 5);
        return id.trim();
    }

    public void deleteEntities(String id) {
        CustomProperties customProperties = customPropertiesRepository.findFirstByValue(id);
        CustomInsert customInsert = customProperties.getCustomInsert();

        List<CustomProperties> customPropertiesList = customPropertiesRepository.findAllByCustomInsert(customInsert);
        for(int i=0; i<customPropertiesList.size(); i++) {
            customPropertiesList.get(i).setUser(null);
            customPropertiesList.get(i).setTableQuery(null);

            customPropertiesRepository.save(customPropertiesList.get(i));
            customPropertiesRepository.deleteByCustomInsert(customInsert);
        }
        customInsertRepository.delete(customInsert);
    }

}
