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
import com.cyrildewit.pgc.logic.model.Subgoal;

@Component
public class InMemorySubgoalDao implements SubgoalDao {
    private List<Subgoal> subgoals = new ArrayList<>();

    public InMemorySubgoalDao() {
        subgoals.add(new Subgoal(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "Afspraak inplannen",
                "Fusce pretium sodales elit, quis imperdiet est laoreet at. Vestibulum ut ex nec lectus efficitur congue. Aliquam fermentum massa et leo dapibus aliquam. Nulla porttitor cursus dictum. Pellentesque finibus pharetra erat nec egestas. Integer porttitor lacus id sapien scelerisque lacinia. Phasellus iaculis scelerisque velit, et ornare felis molestie ac. Ut et ante hendrerit, dictum sapien id, suscipit odio. Pellentesque hendrerit est ac ex ullamcorper tristique. Cras vehicula vehicula turpis, et maximus ipsum. Fusce ut massa ut turpis viverra hendrerit. Nam ac lorem elit. Mauris ornare eu nunc sit amet pulvinar. Nullam iaculis erat quam, non scelerisque tellus laoreet vel.",
                LocalDateTime.now(),
                1L,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        subgoals.add(new Subgoal(
                2L,
                UUID.randomUUID(),
                "Inlezen in materiaal",
                "Fusce pretium sodales elit, quis imperdiet est laoreet at. Vestibulum ut ex nec lectus efficitur congue. Aliquam fermentum massa et leo dapibus aliquam. Nulla porttitor cursus dictum. Pellentesque finibus pharetra erat nec egestas. Integer porttitor lacus id sapien scelerisque lacinia. Phasellus iaculis scelerisque velit, et ornare felis molestie ac. Ut et ante hendrerit, dictum sapien id, suscipit odio. Pellentesque hendrerit est ac ex ullamcorper tristique. Cras vehicula vehicula turpis, et maximus ipsum. Fusce ut massa ut turpis viverra hendrerit. Nam ac lorem elit. Mauris ornare eu nunc sit amet pulvinar. Nullam iaculis erat quam, non scelerisque tellus laoreet vel.",
                LocalDateTime.now(),
                1L,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    public List<Subgoal> selectAllSubgoals() {
        return subgoals;
    }

    public List<Subgoal> selectAllSubgoalsForGoal(Goal goal) {
        return subgoals;
    }

    public List<Subgoal> selectAllFirstLevelSubgoalsForGoal(Goal goal) {
        return subgoals;
    }

    public List<Subgoal> selectAllSubgoalsForSubgoal(Subgoal subgoal) {
        return subgoals;
    }

    public long countAllSubgoalsForGoal(Goal goal)
    {
        return subgoals.stream().count();
    }

    public long countAllFistLevelSubgoalsForGoal(Goal goal) {return subgoals.stream().count();}

    public long countAllSubgoalsForSubgoal(Subgoal subgoal)
    {
        return subgoals.stream().count();
    }

    public long countAllFistLevelSubgoalsForSubgoal(Subgoal subgoal) {return subgoals.stream().count();}

    public Optional<Subgoal> findSubgoalById(long id) {
        return subgoals.stream()
                .filter(subgoal -> id == subgoal.getId())
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

    public boolean updateSubgoal(Subgoal subgoal) {
//        goal.setName(Objects.requireNonNull(
//                params[0], "Name cannot be null"));
//        goal.setEmail(Objects.requireNonNull(
//                params[1], "Email cannot be null"));

        subgoals.add(subgoal);

        return true;
    }

    public void deleteSubgoalById(long id) {
        Optional<Subgoal> subgoal = subgoals.stream()
                .filter(streamSubgoal -> id == streamSubgoal.getId())
                .findAny();

        if (subgoal.isPresent()) {
            subgoals.remove(subgoal);
        }
    }

    public void deleteSubgoal(Subgoal subgoal) {
        subgoals.remove(subgoal);
    }

    public Integer determineSubgoalLevel(Subgoal subgoal)
    {
        return 10;
    }
}