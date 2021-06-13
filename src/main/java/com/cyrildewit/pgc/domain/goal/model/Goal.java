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
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlCoachingStylePreferenceDao;
import com.cyrildewit.pgc.application.services.ActivityService;
import com.cyrildewit.pgc.domain.goal.dao.factory.GoalDaoFactory;
import com.cyrildewit.pgc.domain.goal.dao.factory.CoachingStylePreferenceDaoFactory;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.factory.SuggestiveActionDaoFactory;
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
    private CoachingStylePreferenceDao coachingStylePreferenceDao;
    private SuggestiveActionDao suggestiveActionDao;

    public Goal() {}

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

    public Optional<Activity> getLatestActivity()
    {
        return Optional.ofNullable(latestActivity);
    }

    public void setLatestActivity(Activity latestActivity) {
        this.latestActivity = latestActivity;
    }

    public boolean hasMostRecentFrequentActivity()
    {
        CoachingStylePreference coachingStylePreference = getCoachingStylePreference().get();

        Optional<Goal> goalWithMostRecentFrequency = getGoalDao().getGoalWithMostRecentActivity(coachingStylePreference.getSuggestPinGoalBasedOnActivityBeforePeriodDatetime(), LocalDateTime.now());

        return goalWithMostRecentFrequency.isPresent() && goalWithMostRecentFrequency.get().getId() == getId();
    }

    public void analyzeSuggestiveActions() {
        List<SuggestiveAction> suggestiveActions = new ArrayList<SuggestiveAction>();

        if (getUserId() == 0 || getCoachingStylePreference().isEmpty()) {
            return;
        }

        CoachingStylePreference coachingStylePreference = getCoachingStylePreference().get();
        Optional<Activity> optionalLastGoalActivity = getLatestActivity();

        if (optionalLastGoalActivity.isPresent()) {
            LocalDateTime lastGoalActivityDateTime = optionalLastGoalActivity.get().getCreatedAt();

            if (lastGoalActivityDateTime.isBefore(coachingStylePreference.getSuggestDeleteGoalBeforePeriodDatetime())) {
                suggestiveActionDao.insertUniqueSuggestiveAction(
                        new SuggestiveAction(
                                UUID.randomUUID(),
                                SuggestiveActionType.DELETE_GOAL,
                                getUserId(),
                                getId(),
                                0
                        )
                );
            } else if (lastGoalActivityDateTime.isBefore(LocalDateTime.now().minusWeeks(2))) {
                suggestiveActionDao.insertUniqueSuggestiveAction(
                        new SuggestiveAction(
                                UUID.randomUUID(),
                                SuggestiveActionType.CREATE_SUBGOAL,
                                getUserId(),
                                getId(),
                                0
                        )
                );
            }
        }

        if (hasMostRecentFrequentActivity()) {
            suggestiveActionDao.insertUniqueSuggestiveAction(
                    new SuggestiveAction(
                            UUID.randomUUID(),
                            SuggestiveActionType.PIN_GOAL,
                            getUserId(),
                            getId(),
                            0
                    )
            );
        }
    }


//    public void analyzeSuggestiveActions() {
//        List<SuggestiveAction> suggestiveActions = new ArrayList<SuggestiveAction>();
//
//        if (getUserId() == 0 || getCoachingStylePreference().isEmpty()) {
//            return;
//        }
//
//        CoachingStylePreference coachingStylePreference = getCoachingStylePreference().get();
//        Optional<Activity> optionalLastGoalActivity = getLatestActivity();
//
//        if (optionalLastGoalActivity.isPresent()) {
//            LocalDateTime lastGoalActivityDateTime = optionalLastGoalActivity.get().getCreatedAt();
//
//            if (lastGoalActivityDateTime.isBefore(coachingStylePreference.getSuggestDeleteGoalBeforePeriodDatetime())) {
//                addUniqueSuggestiveAction(
//                        new SuggestiveAction(
//                                UUID.randomUUID(),
//                                SuggestiveActionType.DELETE_GOAL,
//                                getUserId(),
//                                getId(),
//                                0
//                        )
//                );
//            } else if (lastGoalActivityDateTime.isBefore(LocalDateTime.now().minusWeeks(2))) {
//                addUniqueSuggestiveAction(
//                        new SuggestiveAction(
//                                UUID.randomUUID(),
//                                SuggestiveActionType.CREATE_SUBGOAL,
//                                getUserId(),
//                                getId(),
//                                0
//                        )
//                );
//            }
//        }
//
//        if (hasMostRecentFrequentActivity()) {
//            addUniqueSuggestiveAction(
//                    new SuggestiveAction(
//                            UUID.randomUUID(),
//                            SuggestiveActionType.PIN_GOAL,
//                            getUserId(),
//                            getId(),
//                            0
//                    )
//            );
//        }
//    }

    private GoalDao getGoalDao() {
        if (goalDao == null) {
            goalDao = GoalDaoFactory.getSqlGoalDao();
        }

        return goalDao;
    }

    public void setGoalDao(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

//    public void addUniqueSuggestiveAction(SuggestiveAction suggestiveAction)
//    {
//        if (hasSuggestiveAction(suggestiveAction)) {
//            return;
//        }
//
//        addSuggestiveAction(suggestiveAction);
//    }

//    public void addSuggestiveAction(SuggestiveAction suggestiveAction){
//        suggestiveActions.add(suggestiveAction);
//    }

//    public boolean hasSuggestiveAction(SuggestiveAction newSuggestiveAction){
//        for (SuggestiveAction suggestiveAction : suggestiveActions) {
//            if (newSuggestiveAction.getType() == suggestiveAction.getType() &&
//                    newSuggestiveAction.getGoalId() == suggestiveAction.getGoalId() &&
//                    newSuggestiveAction.getSubgoalId() == suggestiveAction.getSubgoalId() &&
//                    newSuggestiveAction.getUserId() == suggestiveAction.getUserId()) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    private CoachingStylePreferenceDao getCoachingStylePreferenceDao() {
        if (coachingStylePreferenceDao == null) {
            coachingStylePreferenceDao = CoachingStylePreferenceDaoFactory.getSqlCoachingStylePreferenceDaoFactory();
        }

        return coachingStylePreferenceDao;
    }

    private SuggestiveActionDao getSuggestiveActionDao() {
//        if (suggestiveActionDao == null) {
//            suggestiveActionDao = SuggestiveActionDaoFactory.getSqlSuggestiveActionDaoFactory();
//        }

        return suggestiveActionDao;
    }

    public void setSuggestiveActionDao(SuggestiveActionDao suggestiveActionDao) {
        this.suggestiveActionDao = suggestiveActionDao;
    }
}