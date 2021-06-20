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
import com.cyrildewit.pgc.domain.user.dao.SqlUserDao;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SqlSuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal.CreateSubgoalSuggestiveActionAnalyzer;

import com.cyrildewit.pgc.data.sql.MariaDBDataStore;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreateSubgoalSuggestiveActionAnalyzerTest {
    private SuggestiveActionDao suggestiveActionDao;
    private UserDao userDao;
    private GoalDao goalDao;
    private CoachingStylePreferenceDao coachingStylePreferenceDao;
    private ActivityDao activityDao;
    private MariaDBDataStore mariaDBDataStore;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public CreateSubgoalSuggestiveActionAnalyzerTest(
            SuggestiveActionDao suggestiveActionDao,
            UserDao userDao,
            CoachingStylePreferenceDao coachingStylePreferenceDao,
            GoalDao goalDao,
            ActivityDao activityDao,
            MariaDBDataStore mariaDBDataStore,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.suggestiveActionDao = suggestiveActionDao;
        this.userDao = userDao;
        this.goalDao = goalDao;
        this.coachingStylePreferenceDao = coachingStylePreferenceDao;
        this.activityDao = activityDao;
        this.mariaDBDataStore = mariaDBDataStore;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void itSuggestsAGoalWithLatestActivityOlderThanConfiguredInCoachingStyle() {
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
                true,
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                false,
                -1,
                goal.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(coachingStylePreference);

        // Add activity for goal that's before the specified period
        Activity activity = new Activity(
            UUID.randomUUID(),
                "",
                "Activity description",
                goal,
                user
        );

        // Save activity in datastore
        activityDao.insertActivity(activity);
        activity = activityDao.findActivityByUuid(activity.getUuid()).get();
        activity.setCreatedAt(LocalDateTime.now().minusSeconds(21 * 7 * 24 * 60 * 60 * 60));  // 21 weeks ago
        activityDao.updateActivity(activity);

        goal.setCoachingStylePreference(coachingStylePreference);
        goal.setLatestActivity(activity);

        Optional<SuggestiveAction> suggestiveActionOptional = (new CreateSubgoalSuggestiveActionAnalyzer(goal)).analyze();

        assertTrue(suggestiveActionOptional.isPresent());

        SuggestiveAction suggestiveAction = suggestiveActionOptional.get();

        assertEquals(SuggestiveActionType.CREATE_SUBGOAL, suggestiveActionOptional.get().getType());
    }

    @Test
    void itDoesNotSuggestAGoalWithLatestActivityNewerThanConfiguredInCoachingStyle() {
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
                true,
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                false,
                -1,
                goal.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(coachingStylePreference);
//        coachingStylePreference = coachingStylePreferenceDao.
        // Add activity for goal that's before the specified period
        Activity activity = new Activity(
                UUID.randomUUID(),
                "",
                "Activity description",
                goal,
                user
        );

        // Save activity in datastore
        activityDao.insertActivity(activity);
        activity = activityDao.findActivityByUuid(activity.getUuid()).get();
        activity.setCreatedAt(LocalDateTime.now().minusSeconds(13 * 7 * 24 * 60 * 60 * 60));  // 13 weeks ago
        activityDao.updateActivity(activity);

        goal.setCoachingStylePreference(coachingStylePreference);
        goal.setLatestActivity(activity);

        Optional<SuggestiveAction> suggestiveActionOptional = (new CreateSubgoalSuggestiveActionAnalyzer(goal)).analyze();

        assertTrue(suggestiveActionOptional.isEmpty());
    }

    @Test
    void itDoesNotSuggestAGoalWithLatestActivityOlderThanConfiguredInCoachingStyleWhenDisabled() {
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
                false, // Important change
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                false,
                -1,
                goal.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(coachingStylePreference);

        // Add activity for goal that's before the specified period
        Activity activity = new Activity(
                UUID.randomUUID(),
                "",
                "Activity description",
                goal,
                user
        );

        // Save activity in datastore
        activityDao.insertActivity(activity);
        activity = activityDao.findActivityByUuid(activity.getUuid()).get();
        activity.setCreatedAt(LocalDateTime.now().minusSeconds(21 * 7 * 24 * 60 * 60 * 60));  // 21 weeks ago
        activityDao.updateActivity(activity);

        goal.setCoachingStylePreference(coachingStylePreference);
        goal.setLatestActivity(activity);

        Optional<SuggestiveAction> suggestiveActionOptional = (new CreateSubgoalSuggestiveActionAnalyzer(goal)).analyze();

        assertTrue(suggestiveActionOptional.isEmpty());
    }
}