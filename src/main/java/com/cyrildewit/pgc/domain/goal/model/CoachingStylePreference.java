package com.cyrildewit.pgc.domain.goal.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;

public class CoachingStylePreference extends Model {
    private final long defaultSuggestDeleteGoalBeforePeriod = 7257600L;
    private final long defaultSuggestCreateSubgoalAfterLastActivityBeforePeriod = 7257600L;
    private final long defaultSuggestPinGoalBasedOnActivityBeforePeriod = 7257600L;
    private final long defaultSuggestDeleteSubgoalAfterLastActivityBeforePeriod = 7257600L;
    private final long defaultSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod = 7257600L;

    private long id;

    private UUID uuid;

    private long suggestDeleteGoalBeforePeriod = defaultSuggestDeleteGoalBeforePeriod;

    private long suggestCreateSubgoalAfterLastActivityBeforePeriod = defaultSuggestCreateSubgoalAfterLastActivityBeforePeriod;

    private long suggestPinGoalBasedOnActivityBeforePeriod = defaultSuggestPinGoalBasedOnActivityBeforePeriod;

    private long suggestDeleteSubgoalAfterLastActivityBeforePeriod = defaultSuggestDeleteSubgoalAfterLastActivityBeforePeriod;

    private long suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod = defaultSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;

    private long goalId;

    private Goal goal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CoachingStylePreference() {}

    public CoachingStylePreference(
            UUID uuid,
            long suggestDeleteGoalBeforePeriod,
            long suggestPinGoalBasedOnActivityBeforePeriod,
            long suggestDeleteSubgoalAfterLastActivityBeforePeriod,
            long suggestCreateSubgoalAfterLastActivityBeforePeriod,
            long suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod,
            long goalId
    ) {
        this.uuid = uuid;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
        this.suggestDeleteSubgoalAfterLastActivityBeforePeriod = suggestDeleteSubgoalAfterLastActivityBeforePeriod;
        this.suggestCreateSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalAfterLastActivityBeforePeriod;
        this.suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;
        this.goalId = goalId;
    }

    public CoachingStylePreference(
            long id,
            UUID uuid,
            long suggestDeleteGoalBeforePeriod,
            long suggestPinGoalBasedOnActivityBeforePeriod,
            long suggestDeleteSubgoalAfterLastActivityBeforePeriod,
            long suggestCreateSubgoalAfterLastActivityBeforePeriod,
            long goalId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.uuid = uuid;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
        this.suggestDeleteSubgoalAfterLastActivityBeforePeriod = suggestDeleteSubgoalAfterLastActivityBeforePeriod;
        this.suggestCreateSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalAfterLastActivityBeforePeriod;
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

    public LocalDateTime getSuggestCreateSubgoalAfterLastActivityBeforePeriodDateTime() {
        return LocalDateTime.now().minusSeconds(getSuggestCreateSubgoalAfterLastActivityBeforePeriod());
    }

    public long getSuggestCreateSubgoalAfterLastActivityBeforePeriod() {
        return suggestCreateSubgoalAfterLastActivityBeforePeriod;
    }

    public void setSuggestCreateSubgoalAfterLastActivityBeforePeriod(long suggestCreateSubgoalAfterLastActivityBeforePeriod) {
        this.suggestCreateSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalAfterLastActivityBeforePeriod;
    }

    public LocalDateTime getSuggestPinGoalBasedOnActivityBeforePeriodDatetime() {
        return LocalDateTime.now().minusSeconds(suggestPinGoalBasedOnActivityBeforePeriod);
    }

    public long getSuggestPinGoalBasedOnActivityBeforePeriod() {
        return suggestPinGoalBasedOnActivityBeforePeriod;
    }

    public void setSuggestPinGoalBasedOnActivityStartDateTime(long suggestPinGoalBasedOnActivityBeforePeriod) {
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
    }

    public LocalDateTime getSuggestDeleteSubgoalAfterLastActivityBeforePeriodDateTime() {
        return LocalDateTime.now().minusSeconds(suggestDeleteSubgoalAfterLastActivityBeforePeriod);
    }

    public long getSuggestDeleteSubgoalAfterLastActivityBeforePeriod() {
        return suggestDeleteSubgoalAfterLastActivityBeforePeriod;
    }

    public void setSuggestDeleteSubgoalAfterLastActivityBeforePeriod(long suggestDeleteSubgoalAfterLastActivityBeforePeriod) {
        this.suggestDeleteSubgoalAfterLastActivityBeforePeriod = suggestDeleteSubgoalAfterLastActivityBeforePeriod;
    }

    public LocalDateTime getSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriodDateTime() {
        return LocalDateTime.now().minusSeconds(suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod);
    }

    public long getSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod() {
        return suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;
    }

    public void setSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod(long suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod) {
        this.suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;
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

    public long getGoalId() {
        return goalId;
    }
}