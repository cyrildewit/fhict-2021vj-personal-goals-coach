package com.cyrildewit.pgc.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Goal extends Model {
    private long id;

    private UUID uuid;

    private String title;

    private String description;

    private LocalDateTime deadline;

    private long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Goal() {}

    public Goal(UUID uuid, String title, String description, LocalDateTime deadline, long userId) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.userId = userId;
    }

    public Goal(long id, UUID uuid, String title, String description, LocalDateTime deadline, long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.userId = userId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

//    public function latestActivity()
//    {
//        return
//    }

//    public function analyseSuggestiveActions()
//    {
//
//    }

//    private void analyzeGoal(Goal goal) {
//        LocalDateTime lastGoalActivityDateTime = LocalDateTime.now().minusWeeks(3);
//
//        if (lastGoalActivityDateTime.isBefore(LocalDateTime.now().minusMonths(3))) {
//            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.DELETE_GOAL, user.getId(), goal.getId(), 0));
//        } else if (lastGoalActivityDateTime.isBefore(LocalDateTime.now().minusWeeks(2))) {
//            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.CREATE_SUBGOAL, user.getId(), goal.getId(), 0));
//        }
//
//        boolean goalHasMostRecentActivity = goalService.goalHasMostRecentActivity(goal);
//
//        if (goalHasMostRecentActivity) {
//            System.out.println("Goal has most recent activity");
//        }
//    }
}