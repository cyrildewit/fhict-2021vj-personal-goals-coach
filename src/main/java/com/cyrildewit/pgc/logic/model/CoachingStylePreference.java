package com.cyrildewit.pgc.logic.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.logic.model.Activity;
import com.cyrildewit.pgc.dao.SqlGoalDao;
import com.cyrildewit.pgc.services.ActivityService;
import com.cyrildewit.pgc.services.SuggestiveActionService;
import com.cyrildewit.pgc.enums.SuggestiveActionType;

public class CoachingStylePreference extends Model {
    private final long defaultSuggestDeleteGoalBeforePeriod = 7257600L;

    private long id;

    private UUID uuid;

    private long suggestDeleteGoalBeforePeriod = defaultSuggestDeleteGoalBeforePeriod;

    private long goalId;

    private Goal goal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CoachingStylePreference() {}

    public CoachingStylePreference(UUID uuid, long suggestDeleteGoalBeforePeriod, long goalId) {
        this.uuid = uuid;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.goalId = goalId;
    }

    public CoachingStylePreference(long id, UUID uuid, long suggestDeleteGoalBeforePeriod, long goalId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.goalId = goalId;
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

    public LocalDateTime getSuggestDeleteGoalBeforePeriodDatetime() {
        return LocalDateTime.now().minusSeconds(getSuggestDeleteGoalBeforePeriod());
    }

    public long getSuggestDeleteGoalBeforePeriod() {
        return suggestDeleteGoalBeforePeriod;
    }

    public void setSuggestDeleteGoalBeforePeriod(long suggestDeleteGoalBeforePeriod) {
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
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

    public Optional<Goal> getGoal() {
        return Optional.ofNullable(goal);
    }

    public void setGoal(Goal goal) {
        this.goalId = goalId;
        this.goal = goal;
    }
}