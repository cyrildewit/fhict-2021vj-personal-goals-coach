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

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.User;

@Component
public class InMemoryGoalDao implements GoalDao {
    private List<Goal> goals = new ArrayList<>();

    public InMemoryGoalDao() {
        goals.add(new Goal(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "Aan de hand van mijn functioneringsgesprek ga ik een nieuw financieel plan opstellen voor de lunchkosten op kantoor",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elit nulla, dapibus vel neque a, pretium varius neque. Aliquam fermentum placerat mauris vitae egestas. Nam finibus urna ut nisl malesuada, sed blandit ipsum dignissim. Cras vestibulum hendrerit cursus. Etiam condimentum, ex posuere imperdiet ultrices, lacus sapien sagittis augue, vel rutrum elit mi vel leo. Donec eu porta massa, a ultrices ex. Suspendisse vel hendrerit lorem, a laoreet ipsum.",
                LocalDateTime.now()
        ));

        goals.add(new Goal(
                2L,
                UUID.randomUUID(),
                "Ik ga een uur per dag online artikelen lezen om me zo voor te bereiden op mijn de cursus op werk",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin elit nulla, dapibus vel neque a, pretium varius neque. Aliquam fermentum placerat mauris vitae egestas. Nam finibus urna ut nisl malesuada, sed blandit ipsum dignissim. Cras vestibulum hendrerit cursus. Etiam condimentum, ex posuere imperdiet ultrices, lacus sapien sagittis augue, vel rutrum elit mi vel leo. Donec eu porta massa, a ultrices ex. Suspendisse vel hendrerit lorem, a laoreet ipsum.",
                LocalDateTime.now()
        ));
    }

    public List<Goal> selectAllGoals() {
        return goals;
    }

    public List<Goal> selectAllGoalsForUser(User user) {
        return goals;
    }

    public Optional<Goal> findGoalById(Integer id) {
        return goals.stream()
                .filter(goal -> id.equals(goal.getId()))
                .findAny();
    }

    public Optional<Goal> findGoalByUuid(UUID uuid) {
        return goals.stream()
                .filter(goal -> uuid.equals(goal.getUuid()))
                .findAny();
    }

    public void insertGoal(Goal goal) {
        goals.add(goal);
    }

    public void updateGoal(Goal goal, String[] params) {
//        goal.setName(Objects.requireNonNull(
//                params[0], "Name cannot be null"));
//        goal.setEmail(Objects.requireNonNull(
//                params[1], "Email cannot be null"));

        goals.add(goal);
    }

    public void deleteGoalById(Integer id) {
        Optional<Goal> goal = goals.stream()
                .filter(streamGoal -> id.equals(streamGoal.getId()))
                .findAny();

        if (goal.isPresent()) {
            goals.remove(goal);
        }
    }

    public void deleteGoal(Goal goal) {
        goals.remove(goal);
    }

    public Long getTotalGoalsCountForUser(User user)
    {
        return goals.stream().count();
    }
}