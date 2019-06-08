package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class TableQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "builded_table_name")
    private String buildedName;

    @Column(name = "select_query")
    private String selectQuery;

    @Column(name = "create_query")
    private String createQuery;

    @Column(name = "table_name_before")
    private String tableNameBefore;

    @Column(name = "amount_columns")
    private int amountColumns;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "tableQuery")
    @Transient
    private List<CustomProperties> customProperties;

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

    public String getCreateQuery() {
        return createQuery;
    }

    public void setCreateQuery(String createQuery) {
        this.createQuery = createQuery;
    }

    public int getAmountColumns() {
        return amountColumns;
    }

    public void setAmountColumns(int amountColumns) {
        this.amountColumns = amountColumns;
    }
}
