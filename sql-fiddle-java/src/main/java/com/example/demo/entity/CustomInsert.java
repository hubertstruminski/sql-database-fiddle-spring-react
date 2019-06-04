package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class CustomInsert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "insert_query")
    private String insertQuery;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customInsert")
    @Transient
    List<CustomProperties> customProperties;

    public CustomInsert() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsertQuery() {
        return insertQuery;
    }

    public void setInsertQuery(String insertQuery) {
        this.insertQuery = insertQuery;
    }

    public List<CustomProperties> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(List<CustomProperties> customProperties) {
        this.customProperties = customProperties;
    }
}
