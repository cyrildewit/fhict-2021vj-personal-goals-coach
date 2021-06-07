package com.cyrildewit.pgc.domain.goal.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cyrildewit.pgc.domain.Model;

public class Subgoal extends Model {
    private long id;

    private UUID uuid;

    private String title;

    private String description;

    private LocalDateTime deadline;

    private long goalId;

    private long parentSubgoalId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Subgoal() {}

    public Subgoal(UUID uuid, String title, String description, LocalDateTime deadline, long goalId, long parentSubgoalId) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.goalId = goalId;
        this.parentSubgoalId = parentSubgoalId;
    }

    public Subgoal(long id, UUID uuid, String title, String description, LocalDateTime deadline, long goalId, long parentSubgoalId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.goalId = goalId;
        this.parentSubgoalId = parentSubgoalId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    public long getParentSubgoalId() {
        return parentSubgoalId;
    }

    public void setParentSubgoalId(long parentSubgoalId) {
        this.parentSubgoalId = parentSubgoalId;
    }

    public boolean hasGoal()
    {
        return goalId > 0;
    }

    public boolean hasParentSubgoal()
    {
        return parentSubgoalId > 0;
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

//    private void analyzeSubgoal(Subgoal subgoal) {
//        LocalDateTime lastSubgoalActivityDateTime = LocalDateTime.now().minusMonths(4);
//
//        if (lastSubgoalActivityDateTime.isBefore(LocalDateTime.now().minusMonths(3))) {
//            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.DELETE_SUBGOAL, user.getId(), subgoal.getGoalId(), subgoal.getId()));
//        } else if (lastSubgoalActivityDateTime.isBefore(LocalDateTime.now().minusWeeks(2))) {
//            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.CREATE_SUBGOAL_FOR_SUBGOAL, user.getId(), subgoal.getGoalId(), subgoal.getId()));
//        }
//    }
}