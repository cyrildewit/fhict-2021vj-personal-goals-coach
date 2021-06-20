package com.cyrildewit.pgc.domain.goal.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.SuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.subgoal.DeleteSubgoalSuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.subgoal.CreateSubgoalForSubgoalSuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;

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

    private Optional<Goal> goal;

    private Optional<Activity> latestActivity = Optional.empty();

    private SubgoalDao subgoalDao;
    private SuggestiveActionDao suggestiveActionDao;
    private ActivityDao activityDao;

    public Subgoal() {
    }

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

    public Optional<Goal> getGoal() {
        return goal;
    }

    public long getParentSubgoalId() {
        return parentSubgoalId;
    }

    public void setParentSubgoalId(long parentSubgoalId) {
        this.parentSubgoalId = parentSubgoalId;
    }

    public boolean hasGoal() {
        return goalId > 0;
    }

    public boolean hasParentSubgoal() {
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

    public Optional<Activity> getLatestActivity() {
        if (latestActivity.isEmpty()) {
            this.latestActivity = activityDao.selectLatestActivityForSubject(this);
        }

        return latestActivity;
    }

    public void setLatestActivity(Activity latestActivity) {
        this.latestActivity = Optional.of(latestActivity);
    }

    public void analyzeSuggestiveActions() {
        List<SuggestiveActionAnalyzer> suggestiveActionAnalyzers = new ArrayList<SuggestiveActionAnalyzer>();
        suggestiveActionAnalyzers.add(new DeleteSubgoalSuggestiveActionAnalyzer(this));
        suggestiveActionAnalyzers.add(new CreateSubgoalForSubgoalSuggestiveActionAnalyzer(this));

        for (SuggestiveActionAnalyzer suggestiveActionAnalyzer : suggestiveActionAnalyzers) {
            suggestiveActionAnalyzer.analyze().ifPresent(suggestiveAction -> suggestiveActionDao.insertUniqueSuggestiveAction(suggestiveAction));
        }
    }

    public void setSuggestiveActionDao(SuggestiveActionDao suggestiveActionDao) {
        this.suggestiveActionDao = suggestiveActionDao;
    }

    public void setSubgoalDao(SubgoalDao subgoalDao) {
        this.subgoalDao = subgoalDao;
    }

    public void setGoal(Goal goal) {
        this.goal = Optional.ofNullable(goal);
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }
}