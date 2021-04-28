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
import com.cyrildewit.pgc.models.Subgoal;

@Component
public class InMemorySubgoalDao implements SubgoalDao {
    private List<Subgoal> subgoals = new ArrayList<>();

    public InMemorySubgoalDao() {
        subgoals.add(new Subgoal(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "Six Summit",
                "Lorem ipsum",
                LocalDateTime.now()
        ));

        subgoals.add(new Subgoal(
                2L,
                UUID.randomUUID(),
                "Six Summit",
                "Lorem ipsum",
                LocalDateTime.now()
        ));
    }

    public List<Subgoal> selectAllSubgoals() {
        return subgoals;
    }

    public List<Subgoal> selectAllSubgoalsForGoal(Goal goal) {
        return subgoals;
    }

    public Long countAllSubgoalsForGoal(Goal goal)
    {
        return subgoals.stream().count();
    }

    public Optional<Subgoal> findSubgoalById(Long id) {
        return subgoals.stream()
                .filter(subgoal -> id.equals(subgoal.getId()))
                .findAny();
    }

    public Optional<Subgoal> findSubgoalByUuid(UUID uuid) {
        return subgoals.stream()
                .filter(subgoal -> uuid.equals(subgoal.getUuid()))
                .findAny();
    }

    public void insertSubgoal(Subgoal subgoal) {
        subgoals.add(subgoal);
    }

    public void updateSubgoal(Subgoal subgoal, String[] params) {
//        goal.setName(Objects.requireNonNull(
//                params[0], "Name cannot be null"));
//        goal.setEmail(Objects.requireNonNull(
//                params[1], "Email cannot be null"));

        subgoals.add(subgoal);
    }

    public void deleteSubgoalById(Long id) {
        Optional<Subgoal> subgoal = subgoals.stream()
                .filter(streamSubgoal -> id.equals(streamSubgoal.getId()))
                .findAny();

        if (subgoal.isPresent()) {
            subgoals.remove(subgoal);
        }
    }

    public void deleteSubgoal(Subgoal subgoal) {
        subgoals.remove(subgoal);
    }
}