package com.cyrildewit.pgc.domain.goal.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;

public class CoachingStylePreference extends Model {
    private final long defaultSuggestDeleteGoalBeforePeriod = 7257600L;
    private final long defaultSuggestPinGoalBasedOnActivityBeforePeriod = 7257600L;

    private long id;

    private UUID uuid;

    private long suggestDeleteGoalBeforePeriod = defaultSuggestDeleteGoalBeforePeriod;

    private long suggestPinGoalBasedOnActivityBeforePeriod = defaultSuggestPinGoalBasedOnActivityBeforePeriod;

    private long goalId;

    private Goal goal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CoachingStylePreference() {}

    public CoachingStylePreference(UUID uuid, long suggestDeleteGoalBeforePeriod, long suggestPinGoalBasedOnActivityBeforePeriod, long goalId) {
        this.uuid = uuid;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
        this.goalId = goalId;
    }

    public CoachingStylePreference(long id, UUID uuid, long suggestDeleteGoalBeforePeriod, long suggestPinGoalBasedOnActivityBeforePeriod, long goalId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
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

    public LocalDateTime gettSuggestPinGoalBasedOnActivityBeforePeriodDatetime() {
        return LocalDateTime.now().minusSeconds(suggestPinGoalBasedOnActivityBeforePeriod);
    }

    public long getSuggestPinGoalBasedOnActivityBeforePeriod() {
        return suggestPinGoalBasedOnActivityBeforePeriod;
    }

    public void setSuggestPinGoalBasedOnActivityStartDateTime(long suggestPinGoalBasedOnActivityBeforePeriod) {
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
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