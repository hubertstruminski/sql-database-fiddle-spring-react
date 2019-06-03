package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class TableQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "builded_table_name")
    private String buildedName;

    @Column(name = "select_query")
    private String selectQuery;

    @Column(name = "table_name_before")
    private String tableNameBefore;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public TableQuery() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSelectQuery() {
        return selectQuery;
    }

    public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

    public String getBuildedName() {
        return buildedName;
    }

    public void setBuildedName(String buildedName) {
        this.buildedName = buildedName;
    }

    public String getTableNameBefore() {
        return tableNameBefore;
    }

    public void setTableNameBefore(String tableNameBefore) {
        this.tableNameBefore = tableNameBefore;
    }
}
