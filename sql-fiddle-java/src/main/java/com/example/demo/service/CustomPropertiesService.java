package com.example.demo.service;

import com.example.demo.entity.CustomProperties;
import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.repository.CustomPropertiesRepository;
import com.example.demo.repository.TableQueryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomPropertiesService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableQueryRepository tableQueryRepository;

    @Autowired
    private CustomPropertiesRepository customPropertiesRepository;

    public String[][] getTable(Long id, String userName) {
        User userByUserName = userRepository.findByUserName(userName);

        if(userByUserName == null) {
            throw new NoSuchElementException("User '" + userName + "' does not exists");
        }

        TableQuery foundedTableQuery = tableQueryRepository.findFirstById(id);

        if(foundedTableQuery == null) {
            throw new NoSuchElementException("Given table does not exists.");
        }

        List<CustomProperties> customPropertiesList = customPropertiesRepository
                .findAllByUserAndTableQueryOrderByCreateAtAsc(userByUserName, foundedTableQuery);

        String buildedTableName = foundedTableQuery.getBuildedName();
        int amountOfColumns = tableQueryRepository.findByBuildedName(buildedTableName).getAmountColumns();

        return processDataToArray(customPropertiesList, amountOfColumns);
    }

    private String[][] processDataToArray(List<CustomProperties> customPropertiesList, int amountOfColumns) {
        int rows = customPropertiesList.size() / amountOfColumns;
        int columns = amountOfColumns;

        String[][] FieldsAndValuesArray = new String[rows + 1][columns];
        int index = 0;
        for(int i=0; i<rows+1; i++) {
            if(i == 0) {
                for(int j=0; j<columns; j++) {
                    FieldsAndValuesArray[i][j] = customPropertiesList.get(j).getField().toUpperCase();
                }
                continue;
            }
            for(int j=0; j<columns; j++) {
                FieldsAndValuesArray[i][j] = customPropertiesList.get(index).getValue();
                index++;
            }
        }
        return FieldsAndValuesArray;
    }

    public void update(String id) {
        CustomProperties customProperties = customPropertiesRepository.findFirstByValue(id);
        customPropertiesRepository.save(customProperties);
    }
}
