package com.cyrildewit.pgc.domain.goal.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlGoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlCoachingStylePreferenceDao;
import com.cyrildewit.pgc.application.services.ActivityService;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.SuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal.PinGoalSuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal.DeleteGoalSuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal.CreateSubgoalSuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.SuggestiveActionAnalyzer;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;

import com.cyrildewit.pgc.application.services.SuggestiveActionService;

public class Goal extends Model {
    private long id;

    private UUID uuid;

    private String title;

    private String description;

    private LocalDateTime deadline;

    private long userId;

    private Optional<User> user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long coachingStylePreferenceId;

    private CoachingStylePreference coachingStylePreference;

    private Activity latestActivity;

    private List<SuggestiveAction> suggestiveActions = new ArrayList<SuggestiveAction>();

    private GoalDao goalDao;
    private SubgoalDao subgoalDao;
    private CoachingStylePreferenceDao coachingStylePreferenceDao;
    private SuggestiveActionDao suggestiveActionDao;
    private ActivityDao activityDao;

    public Goal() {
    }

    public Goal(UUID uuid, String title, String description, LocalDateTime deadline, long userId) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.userId = userId;
        this.user = Optional.empty();
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

    public Optional<User> getUser() {
        return user;
    }

    public void setUser(Optional<User> user) {
        this.user = user;
    }

    public Optional<CoachingStylePreference> getCoachingStylePreference() {
        if (coachingStylePreference == null) {
            Optional<CoachingStylePreference> fetchedCoachingStylePreference = getCoachingStylePreferenceDao().findCoachingSytlePreferenceByGoal(this);

            if (fetchedCoachingStylePreference.isPresent()) {
                setCoachingStylePreference(fetchedCoachingStylePreference.get());
            }
        }

        return Optional.ofNullable(coachingStylePreference);
    }

    public void setCoachingStylePreference(CoachingStylePreference coachingStylePreference) {
        this.coachingStylePreference = coachingStylePreference;
    }

    public Optional<Activity> getLatestActivity() {
        return Optional.ofNullable(latestActivity);
    }

    public void setLatestActivity(Activity latestActivity) {
        this.latestActivity = latestActivity;
    }

    public boolean hasMostRecentFrequentActivity() {
        CoachingStylePreference coachingStylePreference = getCoachingStylePreference().get();

        Optional<Goal> goalWithMostRecentFrequency = getGoalDao().getGoalWithMostRecentActivityForUser(user.get(), coachingStylePreference.getSuggestPinGoalBasedOnActivityBeforePeriodDatetime(), LocalDateTime.now());

        return goalWithMostRecentFrequency.isPresent() && goalWithMostRecentFrequency.get().getId() == getId();
    }

    // gesprek:
    // - verbetepunten software
    // - hoe verlietp het zemesters

    public void analyzeSuggestiveActions() {
        List<SuggestiveActionAnalyzer> suggestiveActionAnalyzers = new ArrayList<SuggestiveActionAnalyzer>();
        suggestiveActionAnalyzers.add(new PinGoalSuggestiveActionAnalyzer(this));
        suggestiveActionAnalyzers.add(new DeleteGoalSuggestiveActionAnalyzer(this));
        suggestiveActionAnalyzers.add(new CreateSubgoalSuggestiveActionAnalyzer(this));

        for (SuggestiveActionAnalyzer suggestiveActionAnalyzer : suggestiveActionAnalyzers) {
            suggestiveActionAnalyzer.analyze().ifPresent(suggestiveAction -> suggestiveActionDao.insertUniqueSuggestiveAction(suggestiveAction));
        }

        for (Subgoal subgoal : subgoalDao.selectAllSubgoalsForGoal(this)) {
            subgoal.setSuggestiveActionDao(suggestiveActionDao);
            subgoal.setSubgoalDao(subgoalDao);
            subgoal.setActivityDao(activityDao);
            subgoal.setGoal(this);

            subgoal.analyzeSuggestiveActions();
        }
    }

    private GoalDao getGoalDao() {
        return goalDao;
    }

    public void setGoalDao(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    private CoachingStylePreferenceDao getCoachingStylePreferenceDao() {
        return coachingStylePreferenceDao;
    }

    private SuggestiveActionDao getSuggestiveActionDao() {
        return suggestiveActionDao;
    }

    public void setSuggestiveActionDao(SuggestiveActionDao suggestiveActionDao) {
        this.suggestiveActionDao = suggestiveActionDao;
    }

    public void setSubgoalDao(SubgoalDao subgoalDao) {
        this.subgoalDao = subgoalDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }
}