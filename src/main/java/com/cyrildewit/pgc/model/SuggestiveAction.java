package com.cyrildewit.pgc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cyrildewit.pgc.enums.SuggestiveActionType;

public class SuggestiveAction {
    private long id;

    private UUID uuid;

    private SuggestiveActionType type;

    private long userId;

    private long goalId;

    private long subgoalId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public SuggestiveAction() {}

    public SuggestiveAction(UUID uuid, SuggestiveActionType type, long userId, long goalId, long subgoalId) {
        this.uuid = uuid;
        this.type = type;
        this.userId = userId;
        this.goalId = goalId;
        this.subgoalId = subgoalId;
    }

    public SuggestiveAction(long id, UUID uuid, SuggestiveActionType type, long userId, long goalId, long subgoalId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.type = type;
        this.userId = userId;
        this.goalId = goalId;
        this.subgoalId = subgoalId;
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

    public SuggestiveActionType getType() {
        return type;
    }

    public void setType(SuggestiveActionType type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    public long getSubgoalId() {
        return subgoalId;
    }

    public void setSubgoalId(long subgoalId) {
        this.subgoalId = subgoalId;
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