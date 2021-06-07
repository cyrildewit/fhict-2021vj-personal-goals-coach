package com.cyrildewit.pgc.logic.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

public class Activity {
    private final Optional<String> DEFAULT_LOG_NAME = Optional.empty();

    private long id;

    private UUID uuid;

    private String logName;

    private String description;

    private long subjectId;

    private String subjectType;

    private long causerId;

    private String causerType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Activity() {}

    public Activity(UUID uuid, String logName, String description, long subjectId, String subjectType, long causerId, String causerType) {
        this.uuid = uuid;
        this.logName = logName;
        this.description = description;
        this.subjectId = subjectId;
        this.subjectType = subjectType;
    }

    public Activity(UUID uuid, String logName, String description, Model subject, Model causer) {
        this.uuid = uuid;
        this.logName = logName;
        this.description = description;
        this.setSubject(subject);
        this.setCauser(causer);
    }

    public Activity(long id, UUID uuid, String logName, String description, long subjectId, String subjectType, long causerId, String causerType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.logName = logName;
        this.description = description;
        this.subjectId = subjectId;
        this.subjectType = subjectType;
        this.causerId = causerId;
        this.causerType = causerType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject(Model subject) {
        setSubjectId(subject.getId());
        setSubjectType(subject.getMorphClass());
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public void setCauser(Model causer) {
        setCauserId(causer.getId());
        setCauserType(causer.getMorphClass());
    }

    public long getCauserId() {
        return causerId;
    }

    public void setCauserId(long causerId) {
        this.causerId = causerId;
    }

    public String getCauserType() {
        return causerType;
    }

    public void setCauserType(String causerType) {
        this.causerType = causerType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}