package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "custom_properties")
public class CustomProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "field")
    private String field;

    @Column(name = "value")
    private String value;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "custom_insert_id")
    @JsonIgnore
    private CustomInsert customInsert;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_query_id")
    @JsonIgnore
    private TableQuery tableQuery;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createAt;

    public CustomProperties() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CustomInsert getCustomInsert() {
        return customInsert;
    }

    public void setCustomInsert(CustomInsert customInsert) {
        this.customInsert = customInsert;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public TableQuery getTableQuery() {
        return tableQuery;
    }

    public void setTableQuery(TableQuery tableQuery) {
        this.tableQuery = tableQuery;
    }
}
