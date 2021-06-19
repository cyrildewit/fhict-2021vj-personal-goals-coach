package com.cyrildewit.pgc.domain.user.model;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.user.dao.UserDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;

public class User extends Model {
    private long id;

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private LocalDateTime emailVerifiedAt;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Goal> goals = new ArrayList<Goal>();

    private GoalDao goalDao;
    private SuggestiveActionDao suggestiveActionDao;
    private ActivityDao activityDao;
    private SubgoalDao subgoalDao;
    private UserDao userDao;

    public User() {}

    public User(UUID uuid, String firstName, String lastName, String phoneNumber, String email, LocalDateTime emailVerifiedAt, String password) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.password = password;
    }

    public User(long id, UUID uuid, String firstName, String lasttName, String phoneNumber, String email, LocalDateTime emailVerifiedAt, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lasttName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.password = password;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return Stream.of(firstName, lastName)
                .filter(x -> x != null && !x.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public void analyzeSuggestiveActions()
    {
        for (Goal goal : getGoals()) {
            goal.setGoalDao(goalDao);
            goal.setSubgoalDao(subgoalDao);
            goal.setSuggestiveActionDao(suggestiveActionDao);
            goal.setActivityDao(activityDao);

            goal.analyzeSuggestiveActions();
        }
    }

    private GoalDao getGoalDao() {
        return goalDao;
    }

    public void setGoalDao(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public void setSuggestiveActionDao(SuggestiveActionDao suggestiveActionDao) {
        this.suggestiveActionDao = suggestiveActionDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setSubgoalDao(SubgoalDao subgoalDao) {
        this.subgoalDao = subgoalDao;
    }
}