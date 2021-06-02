package com.idit.designer.project_manager.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.lang.NonNull;

import java.time.ZonedDateTime;

@Data
@Document
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    @Field
    private String id;

    @CreatedDate
    @NonNull
    private ZonedDateTime createdOn;

    @NonNull
    @Field
    private String name;


    @NonNull
    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(@NonNull ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Project(String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
