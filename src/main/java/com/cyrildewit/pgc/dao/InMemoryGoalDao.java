package com.cyrildewit.pgc.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Component;

import com.cyrildewit.pgc.logic.model.Goal;
import com.cyrildewit.pgc.logic.model.User;

@Component
public class InMemoryGoalDao implements GoalDao {
    private List<Goal> goals = new ArrayList<>();

    public InMemoryGoalDao() {
        goals.add(new Goal(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "Aan de hand van mijn functioneringsgesprek ga ik een nieuw financieel plan opstellen voor de lunchkosten op kantoor",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elit nulla, dapibus vel neque a, pretium varius neque. Aliquam fermentum placerat mauris vitae egestas. Nam finibus urna ut nisl malesuada, sed blandit ipsum dignissim. Cras vestibulum hendrerit cursus. Etiam condimentum, ex posuere imperdiet ultrices, lacus sapien sagittis augue, vel rutrum elit mi vel leo. Donec eu porta massa, a ultrices ex. Suspendisse vel hendrerit lorem, a laoreet ipsum.",
                LocalDateTime.now(),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        goals.add(new Goal(
                2L,
                UUID.randomUUID(),
                "Ik ga een uur per dag online artikelen lezen om me zo voor te bereiden op mijn de cursus op werk",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elit nulla, dapibus vel neque a, pretium varius neque. Aliquam fermentum placerat mauris vitae egestas. Nam finibus urna ut nisl malesuada, sed blandit ipsum dignissim. Cras vestibulum hendrerit cursus. Etiam condimentum, ex posuere imperdiet ultrices, lacus sapien sagittis augue, vel rutrum elit mi vel leo. Donec eu porta massa, a ultrices ex. Suspendisse vel hendrerit lorem, a laoreet ipsum.",
                LocalDateTime.now(),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    public List<Goal> selectAllGoals() {
        return goals;
    }

    public List<Goal> selectAllGoalsForUser(User user) {
        return goals;
    }

    public Optional<Goal> findGoalById(long id) {
        return goals.stream()
                .filter(goal -> id == goal.getId())
                .findAny();
    }

    public Optional<Goal> findGoalByUuid(UUID uuid) {
        return goals.stream()
                .filter(goal -> uuid.equals(goal.getUuid()))
                .findAny();
    }

    public List<Goal> findGoalByIds(List<Long> ids) {

        return goals;
    }

    public void insertGoal(Goal goal) {
        goals.add(goal);
    }

    public boolean updateGoal(Goal goal) {
//        goal.setName(Objects.requireNonNull(
//                params[0], "Name cannot be null"));
//        goal.setEmail(Objects.requireNonNull(
//                params[1], "Email cannot be null"));

        goals.add(goal);

        return true;
    }

    public void deleteGoalById(long id) {
        Optional<Goal> goal = goals.stream()
                .filter(streamGoal -> id == streamGoal.getId())
                .findAny();

        if (goal.isPresent()) {
            goals.remove(goal);
        }
    }

    public void deleteGoal(Goal goal) {
        goals.remove(goal);
    }

    public long getTotalGoalsCountForUser(User user)
    {
        return goals.stream().count();
    }

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start) {
        return Optional.empty();
    }

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start, LocalDateTime end) {
        Optional<Goal> goal = Optional.empty();
        return goal;
    }

    public Optional<Goal> getGoalWithMostRecentFrequentActivityForUser(User user, int days, LocalDateTime start) {
        Optional<Goal> goal = Optional.empty();
        return goal;
    }

    public Optional<Goal> getGoalWithMostRecentFrequentActivityForUser(User user, int days, LocalDateTime start, LocalDateTime end) {
        Optional<Goal> goal = Optional.empty();
        return goal;
    }
}