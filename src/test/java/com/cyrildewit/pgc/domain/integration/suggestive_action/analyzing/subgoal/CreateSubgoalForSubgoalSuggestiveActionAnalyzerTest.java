package com.cyrildewit.pgc.domain.suggestive_action.goal.analyzing;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.user.dao.UserDao;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.subgoal.CreateSubgoalForSubgoalSuggestiveActionAnalyzer;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreateSubgoalForSubgoalSuggestiveActionAnalyzerTest {
    private SuggestiveActionDao suggestiveActionDao;
    private UserDao userDao;
    private GoalDao goalDao;
    private SubgoalDao subgoalDao;
    private CoachingStylePreferenceDao coachingStylePreferenceDao;
    private ActivityDao activityDao;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public CreateSubgoalForSubgoalSuggestiveActionAnalyzerTest(
            SuggestiveActionDao suggestiveActionDao,
            UserDao userDao,
            CoachingStylePreferenceDao coachingStylePreferenceDao,
            GoalDao goalDao,
            SubgoalDao subgoalDao,
            ActivityDao activityDao,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.suggestiveActionDao = suggestiveActionDao;
        this.userDao = userDao;
        this.goalDao = goalDao;
        this.subgoalDao = subgoalDao;
        this.coachingStylePreferenceDao = coachingStylePreferenceDao;
        this.activityDao = activityDao;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void itSuggestsASubgoalWithLatestActivityOlderThanConfiguredInCoachingStyle() {
        // Data clean up
        userDao.truncate();
        goalDao.truncate();
        activityDao.truncate();
        coachingStylePreferenceDao.truncate();
        suggestiveActionDao.truncate();

        // Data setup
        User user = new User(
                UUID.randomUUID(),
                "John",
                "Doe",
                "062993939",
                "john@example.com",
                LocalDateTime.now(),
                "password"
        );

        // Save user in datastore
        userDao.insertUser(user);

        // Refetch user, so it has all the data
        user = userDao.findUserByUuid(user.getUuid()).get();

        Goal goal = new Goal(
                UUID.randomUUID(),
                "Test Goal",
                "Description",
                LocalDateTime.now(),
                user.getId()
        );

        // Save goal in datastore
        goalDao.insertGoal(goal);

        // Refetch goal, so it has all the data
        goal = goalDao.findGoalByUuid(goal.getUuid()).get();

        CoachingStylePreference coachingStylePreference = new CoachingStylePreference(
                UUID.randomUUID(),
                false,
                -1, 
                false,
                -1, 
                false,
                -1, 
                false,
                -1, 
                true,
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                goal.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(coachingStylePreference);

        Subgoal subgoal = new Subgoal(
                UUID.randomUUID(),
                "Test Subgoal",
                "Description",
                LocalDateTime.now(),
                user.getId(),
                0
        );

        // Save goal in datastore
        subgoalDao.insertSubgoal(subgoal);

        // Refetch goal, so it has all the data
        subgoal = subgoalDao.findSubgoalByUuid(subgoal.getUuid()).get();
        subgoal.setGoal(goal);

        // Add activity for goal that's before the specified period
        Activity activity = new Activity(
            UUID.randomUUID(),
                "",
                "Activity description",
                subgoal,
                user
        );

        // Save activity in datastore
        activityDao.insertActivity(activity);
        activity = activityDao.findActivityByUuid(activity.getUuid()).get();
        activity.setCreatedAt(LocalDateTime.now().minusSeconds(21 * 7 * 24 * 60 * 60 * 60));  // 21 weeks ago
        activityDao.updateActivity(activity);

        goal.setCoachingStylePreference(coachingStylePreference);
        subgoal.setLatestActivity(activity);

        Optional<SuggestiveAction> suggestiveActionOptional = (new CreateSubgoalForSubgoalSuggestiveActionAnalyzer(subgoal)).analyze();

        assertTrue(suggestiveActionOptional.isPresent());

        SuggestiveAction suggestiveAction = suggestiveActionOptional.get();

        assertEquals(SuggestiveActionType.CREATE_SUBGOAL_FOR_SUBGOAL, suggestiveActionOptional.get().getType());
    }

    @Test
    void itDoesNotSuggestASubgoalWithLatestActivityNewerThanConfiguredInCoachingStyle() {
        // Data clean up
        userDao.truncate();
        goalDao.truncate();
        activityDao.truncate();
        coachingStylePreferenceDao.truncate();
        suggestiveActionDao.truncate();

        // Data setup
        User user = new User(
                UUID.randomUUID(),
                "John",
                "Doe",
                "062993939",
                "john@example.com",
                LocalDateTime.now(),
                "password"
        );

        // Save user in datastore
        userDao.insertUser(user);

        // Refetch user, so it has all the data
        user = userDao.findUserByUuid(user.getUuid()).get();

        Goal goal = new Goal(
                UUID.randomUUID(),
                "Test Goal",
                "Description",
                LocalDateTime.now(),
                user.getId()
        );

        // Save goal in datastore
        goalDao.insertGoal(goal);

        // Refetch goal, so it has all the data
        goal = goalDao.findGoalByUuid(goal.getUuid()).get();

        CoachingStylePreference coachingStylePreference = new CoachingStylePreference(
                UUID.randomUUID(),
                false,
                -1, 
                false,
                -1, 
                false,
                -1, 
                false,
                -1, 
                true,
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                goal.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(coachingStylePreference);

        Subgoal subgoal = new Subgoal(
                UUID.randomUUID(),
                "Test Subgoal",
                "Description",
                LocalDateTime.now(),
                user.getId(),
                0
        );

        // Save goal in datastore
        subgoalDao.insertSubgoal(subgoal);

        // Refetch goal, so it has all the data
        subgoal = subgoalDao.findSubgoalByUuid(subgoal.getUuid()).get();
        subgoal.setGoal(goal);

        // Add activity for goal that's before the specified period
        Activity activity = new Activity(
                UUID.randomUUID(),
                "",
                "Activity description",
                subgoal,
                user
        );

        // Save activity in datastore
        activityDao.insertActivity(activity);
        activity = activityDao.findActivityByUuid(activity.getUuid()).get();
        activity.setCreatedAt(LocalDateTime.now().minusSeconds(11 * 7 * 24 * 60 * 60 * 60));  // 1 weeks ago
        activityDao.updateActivity(activity);

        goal.setCoachingStylePreference(coachingStylePreference);
        subgoal.setLatestActivity(activity);

        Optional<SuggestiveAction> suggestiveActionOptional = (new CreateSubgoalForSubgoalSuggestiveActionAnalyzer(subgoal)).analyze();

        assertTrue(suggestiveActionOptional.isEmpty());
    }

    @Test
    void itDoesNotSuggestASubgoalWithLatestActivityOlderThanConfiguredInCoachingStyleWhenDisabled() {
        // Data clean up
        userDao.truncate();
        goalDao.truncate();
        activityDao.truncate();
        coachingStylePreferenceDao.truncate();
        suggestiveActionDao.truncate();

        // Data setup
        User user = new User(
                UUID.randomUUID(),
                "John",
                "Doe",
                "062993939",
                "john@example.com",
                LocalDateTime.now(),
                "password"
        );

        // Save user in datastore
        userDao.insertUser(user);

        // Refetch user, so it has all the data
        user = userDao.findUserByUuid(user.getUuid()).get();

        Goal goal = new Goal(
                UUID.randomUUID(),
                "Test Goal",
                "Description",
                LocalDateTime.now(),
                user.getId()
        );

        // Save goal in datastore
        goalDao.insertGoal(goal);

        // Refetch goal, so it has all the data
        goal = goalDao.findGoalByUuid(goal.getUuid()).get();

        CoachingStylePreference coachingStylePreference = new CoachingStylePreference(
                UUID.randomUUID(),
                false,
                -1, 
                false,
                -1, 
                false,
                -1, 
                false,
                -1, 
                false, // Important factor
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                goal.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(coachingStylePreference);

        Subgoal subgoal = new Subgoal(
                UUID.randomUUID(),
                "Test Subgoal",
                "Description",
                LocalDateTime.now(),
                user.getId(),
                0
        );

        // Save goal in datastore
        subgoalDao.insertSubgoal(subgoal);

        // Refetch goal, so it has all the data
        subgoal = subgoalDao.findSubgoalByUuid(subgoal.getUuid()).get();
        subgoal.setGoal(goal);

        // Add activity for goal that's before the specified period
        Activity activity = new Activity(
                UUID.randomUUID(),
                "",
                "Activity description",
                subgoal,
                user
        );

        // Save activity in datastore
        activityDao.insertActivity(activity);
        activity = activityDao.findActivityByUuid(activity.getUuid()).get();
        activity.setCreatedAt(LocalDateTime.now().minusSeconds(21 * 7 * 24 * 60 * 60 * 60));  // 21 weeks ago
        activityDao.updateActivity(activity);

        goal.setCoachingStylePreference(coachingStylePreference);
        subgoal.setLatestActivity(activity);

        Optional<SuggestiveAction> suggestiveActionOptional = (new CreateSubgoalForSubgoalSuggestiveActionAnalyzer(subgoal)).analyze();

        assertTrue(suggestiveActionOptional.isEmpty());
    }
}