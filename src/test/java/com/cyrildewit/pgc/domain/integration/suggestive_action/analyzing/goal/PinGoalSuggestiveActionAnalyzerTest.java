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
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal.PinGoalSuggestiveActionAnalyzer;

import com.cyrildewit.pgc.data.sql.MariaDBDataStore;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PinGoalSuggestiveActionAnalyzerTest {
    private SuggestiveActionDao suggestiveActionDao;
    private UserDao userDao;
    private GoalDao goalDao;
    private CoachingStylePreferenceDao coachingStylePreferenceDao;
    private ActivityDao activityDao;
    private MariaDBDataStore mariaDBDataStore;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public PinGoalSuggestiveActionAnalyzerTest(
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

        // Goal with 10 activites
        Goal goalOne = new Goal(
                UUID.randomUUID(),
                "Test Goal 1",
                "Description",
                LocalDateTime.now(),
                user.getId()
        );
        Goal goalTwo = new Goal(
                UUID.randomUUID(),
                "Test Goal 2",
                "Description",
                LocalDateTime.now(),
                user.getId()
        );

        // Save goals in datastore
        goalDao.insertGoal(goalOne);
        goalDao.insertGoal(goalTwo);

        // Refetch goal, so it has all the data
        goalOne = goalDao.findGoalByUuid(goalOne.getUuid()).get();
        goalTwo = goalDao.findGoalByUuid(goalTwo.getUuid()).get();

        goalOne.setGoalDao(goalDao);
        goalTwo.setGoalDao(goalDao);

        CoachingStylePreference goalOneCoachingStylePreference = new CoachingStylePreference(
                UUID.randomUUID(),
                -1, // unrelevant
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                -1, // unrelevant
                -1, // unrelevant
                -1, // unrelevant
                goalOne.getId()
        );

        CoachingStylePreference goalTwoCoachingStylePreference = new CoachingStylePreference(
                UUID.randomUUID(),
                -1, // unrelevant
                20 * 7 * 24 * 60 * 60 * 60, // 20 weeks
                -1, // unrelevant
                -1, // unrelevant
                -1, // unrelevant
                goalTwo.getId()
        );

        // Save coaching style preference in data store
        coachingStylePreferenceDao.insertCoachingStylePreference(goalOneCoachingStylePreference);
        coachingStylePreferenceDao.insertCoachingStylePreference(goalTwoCoachingStylePreference);

        // Add activity for goal that's within the specified period
        Activity activity1 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity1);
        activity1 = activityDao.findActivityByUuid(activity1.getUuid()).get();
        activity1.setCreatedAt(LocalDateTime.now().minusSeconds(1 * 7 * 24 * 60 * 60 * 60));  // 1 weeks ago
        activityDao.updateActivity(activity1);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity1);

        // Add activity for goal that's within the specified period
        Activity activity2 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity2);
        activity2 = activityDao.findActivityByUuid(activity2.getUuid()).get();
        activity2.setCreatedAt(LocalDateTime.now().minusSeconds(1 * 7 * 24 * 60 * 60 * 60));  // 1 weeks ago
        activityDao.updateActivity(activity2);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity2);

        // Add activity for goal that's before the specified period
        Activity activity3 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity3);
        activity3 = activityDao.findActivityByUuid(activity3.getUuid()).get();
        activity3.setCreatedAt(LocalDateTime.now().minusSeconds(2 * 7 * 24 * 60 * 60 * 60));  // 2 weeks ago
        activityDao.updateActivity(activity3);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity3);

        // Add activity for goal that's before the specified period
        Activity activity4 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity4);
        activity4 = activityDao.findActivityByUuid(activity4.getUuid()).get();
        activity4.setCreatedAt(LocalDateTime.now().minusSeconds(2 * 7 * 24 * 60 * 60 * 60));  // 2 weeks ago
        activityDao.updateActivity(activity4);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity4);

        // Add activity for goal that's before the specified period
        Activity activity5 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity5);
        activity5 = activityDao.findActivityByUuid(activity5.getUuid()).get();
        activity5.setCreatedAt(LocalDateTime.now().minusSeconds(3 * 7 * 24 * 60 * 60 * 60));  // 3 weeks ago
        activityDao.updateActivity(activity5);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity5);

        // Add activity for goal that's before the specified period
        Activity activity6 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity6);
        activity6 = activityDao.findActivityByUuid(activity6.getUuid()).get();
        activity6.setCreatedAt(LocalDateTime.now().minusSeconds(3 * 7 * 24 * 60 * 60 * 60));  // 23 weeks ago
        activityDao.updateActivity(activity6);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity6);

        // Add activity for goal that's before the specified period
        Activity activity7 = new Activity(UUID.randomUUID(), "", "Activity description", goalOne, user);

        // Save activity in datastore
        activityDao.insertActivity(activity7);
        activity7 = activityDao.findActivityByUuid(activity7.getUuid()).get();
        activity7.setCreatedAt(LocalDateTime.now().minusSeconds(4 * 7 * 24 * 60 * 60 * 60));  // 4 weeks ago
        activityDao.updateActivity(activity7);
        goalOne.setCoachingStylePreference(goalOneCoachingStylePreference);
        goalOne.setLatestActivity(activity7);

        // Add activity for goal that's within the specified period
        Activity goalTwoActivity1 = new Activity(UUID.randomUUID(), "", "Activity description", goalTwo, user);

        // Save activity in datastore
        activityDao.insertActivity(goalTwoActivity1);
        goalTwoActivity1 = activityDao.findActivityByUuid(goalTwoActivity1.getUuid()).get();
        goalTwoActivity1.setCreatedAt(LocalDateTime.now().minusSeconds(1 * 7 * 24 * 60 * 60 * 60));  // 1 weeks ago
        activityDao.updateActivity(goalTwoActivity1);
        goalTwo.setCoachingStylePreference(goalTwoCoachingStylePreference);
        goalTwo.setLatestActivity(goalTwoActivity1);

        // Add activity for goal that's within the specified period
        Activity goalTwoActivity2 = new Activity(UUID.randomUUID(), "", "Activity description", goalTwo, user);

        // Save activity in datastore
        activityDao.insertActivity(goalTwoActivity2);
        goalTwoActivity2 = activityDao.findActivityByUuid(goalTwoActivity2.getUuid()).get();
        goalTwoActivity2.setCreatedAt(LocalDateTime.now().minusSeconds(1 * 7 * 24 * 60 * 60 * 60));  // 1 weeks ago
        activityDao.updateActivity(goalTwoActivity2);
        goalTwo.setCoachingStylePreference(goalTwoCoachingStylePreference);
        goalTwo.setLatestActivity(goalTwoActivity2);

        // Add activity for goal that's before the specified period
        Activity goalTwoActivity3 = new Activity(UUID.randomUUID(), "", "Activity description", goalTwo, user);

        // Save activity in datastore
        activityDao.insertActivity(goalTwoActivity3);
        goalTwoActivity3 = activityDao.findActivityByUuid(goalTwoActivity3.getUuid()).get();
        goalTwoActivity3.setCreatedAt(LocalDateTime.now().minusSeconds(2 * 7 * 24 * 60 * 60 * 60));  // 2 weeks ago
        activityDao.updateActivity(goalTwoActivity3);
        goalTwo.setCoachingStylePreference(goalTwoCoachingStylePreference);
        goalTwo.setLatestActivity(goalTwoActivity3);

        // Add activity for goal that's before the specified period
        Activity goalTwoActivity4 = new Activity(UUID.randomUUID(), "", "Activity description", goalTwo, user);

        // Save activity in datastore
        activityDao.insertActivity(goalTwoActivity4);
        goalTwoActivity4 = activityDao.findActivityByUuid(goalTwoActivity4.getUuid()).get();
        goalTwoActivity4.setCreatedAt(LocalDateTime.now().minusSeconds(2 * 7 * 24 * 60 * 60 * 60));  // 2 weeks ago
        activityDao.updateActivity(goalTwoActivity4);
        goalTwo.setCoachingStylePreference(goalTwoCoachingStylePreference);
        goalTwo.setLatestActivity(goalTwoActivity4);

        // Add activity for goal that's before the specified period
        Activity goalTwoActivity5 = new Activity(UUID.randomUUID(), "", "Activity description", goalTwo, user);

        // Save activity in datastore
        activityDao.insertActivity(goalTwoActivity5);
        goalTwoActivity5 = activityDao.findActivityByUuid(goalTwoActivity5.getUuid()).get();
        goalTwoActivity5.setCreatedAt(LocalDateTime.now().minusSeconds(3 * 7 * 24 * 60 * 60 * 60));  // 3 weeks ago
        activityDao.updateActivity(goalTwoActivity5);
        goalTwo.setCoachingStylePreference(goalTwoCoachingStylePreference);
        goalTwo.setLatestActivity(goalTwoActivity5);

        // Goal one analysis
        Optional<SuggestiveAction> goalOneSuggestiveActionOptional = (new PinGoalSuggestiveActionAnalyzer(goalOne)).analyze();

        assertTrue(goalOneSuggestiveActionOptional.isPresent());

        SuggestiveAction goalOneSuggestiveAction = goalOneSuggestiveActionOptional.get();

        assertEquals(SuggestiveActionType.PIN_GOAL, goalOneSuggestiveAction.getType());
        assertEquals(goalOne.getId(), goalOneSuggestiveAction.getGoalId());

        // Goal two analysis
        Optional<SuggestiveAction> goalTwoSuggestiveActionOptional = (new PinGoalSuggestiveActionAnalyzer(goalTwo)).analyze();

        assertTrue(goalTwoSuggestiveActionOptional.isEmpty());
    }
}