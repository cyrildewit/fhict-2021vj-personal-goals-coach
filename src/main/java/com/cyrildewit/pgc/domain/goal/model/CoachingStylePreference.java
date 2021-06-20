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

    private boolean isSuggestDeleteGoalEnabled;

    private long suggestDeleteGoalBeforePeriod;

    private boolean isSuggestCreateSubgoalEnabled;

    private long suggestCreateSubgoalAfterLastActivityBeforePeriod;

    private boolean isSuggestPinGoalEnabled;

    private long suggestPinGoalBasedOnActivityBeforePeriod;

    private boolean isSuggestDeleteSubgoalEnabled;

    private long suggestDeleteSubgoalAfterLastActivityBeforePeriod;

    private boolean isSuggestCreateSubgoalForSubgoalEnabled;

    private long suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;

    private long goalId;

    private Goal goal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CoachingStylePreference() {}

    public CoachingStylePreference(
            UUID uuid,
            boolean isSuggestDeleteGoalEnabled,
            long suggestDeleteGoalBeforePeriod,
            boolean isSuggestPinGoalEnabled,
            long suggestPinGoalBasedOnActivityBeforePeriod,
            boolean isSuggestDeleteSubgoalEnabled,
            long suggestDeleteSubgoalAfterLastActivityBeforePeriod,
            boolean isSuggestCreateSubgoalEnabled,
            long suggestCreateSubgoalAfterLastActivityBeforePeriod,
            boolean isSuggestCreateSubgoalForSubgoalEnabled,
            long suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod,
            long goalId
    ) {
        this.uuid = uuid;
        this.isSuggestDeleteGoalEnabled = isSuggestDeleteGoalEnabled;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.isSuggestPinGoalEnabled = isSuggestPinGoalEnabled;
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
        this.isSuggestDeleteSubgoalEnabled = isSuggestDeleteSubgoalEnabled;
        this.suggestDeleteSubgoalAfterLastActivityBeforePeriod = suggestDeleteSubgoalAfterLastActivityBeforePeriod;
        this.isSuggestCreateSubgoalEnabled = isSuggestCreateSubgoalEnabled;
        this.suggestCreateSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalAfterLastActivityBeforePeriod;
        this.isSuggestCreateSubgoalForSubgoalEnabled = isSuggestCreateSubgoalForSubgoalEnabled;
        this.suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;
        this.goalId = goalId;
    }

    public CoachingStylePreference(
            long id,
            UUID uuid,
            boolean isSuggestDeleteGoalEnabled,
            long suggestDeleteGoalBeforePeriod,
            boolean isSuggestPinGoalEnabled,
            long suggestPinGoalBasedOnActivityBeforePeriod,
            boolean isSuggestDeleteSubgoalEnabled,
            long suggestDeleteSubgoalAfterLastActivityBeforePeriod,
            boolean isSuggestCreateSubgoalEnabled,
            long suggestCreateSubgoalAfterLastActivityBeforePeriod,
            boolean isSuggestCreateSubgoalForSubgoalEnabled,
            long suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod,
            long goalId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.uuid = uuid;
        this.isSuggestDeleteGoalEnabled = isSuggestDeleteGoalEnabled;
        this.suggestDeleteGoalBeforePeriod = suggestDeleteGoalBeforePeriod;
        this.isSuggestPinGoalEnabled = isSuggestPinGoalEnabled;
        this.suggestPinGoalBasedOnActivityBeforePeriod = suggestPinGoalBasedOnActivityBeforePeriod;
        this.isSuggestDeleteSubgoalEnabled = isSuggestDeleteSubgoalEnabled;
        this.suggestDeleteSubgoalAfterLastActivityBeforePeriod = suggestDeleteSubgoalAfterLastActivityBeforePeriod;
        this.suggestCreateSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalAfterLastActivityBeforePeriod;
        this.isSuggestCreateSubgoalForSubgoalEnabled = isSuggestCreateSubgoalForSubgoalEnabled;
        this.suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod = suggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod;
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

    public boolean isSuggestDeleteGoalEnabled() {
        return isSuggestDeleteGoalEnabled;
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

    public boolean isSuggestCreateSubgoalEnabled() {
        return isSuggestCreateSubgoalEnabled;
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

    public boolean isSuggestPinGoalEnabled() {
        return isSuggestPinGoalEnabled;
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

    public boolean isSuggestDeleteSubgoalEnabled() {
        return isSuggestDeleteSubgoalEnabled;
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

    public boolean isSuggestCreateSubgoalForSubgoalEnabled() {
        return isSuggestCreateSubgoalForSubgoalEnabled;
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