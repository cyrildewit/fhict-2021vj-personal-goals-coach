package com.cyrildewit.pgc.application.view_model.front.goal.index;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cyrildewit.pgc.domain.goal.model.Goal;

public final class GoalIndexDto {
    private long id;
    private UUID uuid;
    private String title;
    private String description;

    public GoalIndexDto(long id, UUID uuid, String title, String description) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
    }

    public static GoalIndexDto fromGoalEntity(Goal goal) {
        return new GoalIndexDto(goal.getId(), goal.getUuid(), goal.getTitle(), goal.getDescription());
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
}