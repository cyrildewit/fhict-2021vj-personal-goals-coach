package com.cyrildewit.pgc.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

//@Entity
public class Subgoal {
    private Long id;

    private UUID uuid;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    @NotNull
    private long goalId;

    @NotNull
    private long parentSubgoalId;

    public Subgoal() {}

    public Subgoal(UUID uuid, String title, String description, LocalDateTime deadline, long goalId, long parentSubgoalId) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.goalId = goalId;
        this.parentSubgoalId = parentSubgoalId;
    }

    public Subgoal(Long id, UUID uuid, String title, String description, LocalDateTime deadline, long goalId, long parentSubgoalId) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.goalId = goalId;
        this.parentSubgoalId = parentSubgoalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

    public Long getParentSubgoalId() {
        return parentSubgoalId;
    }

    public void setParentSubgoalId(Long parentSubgoalId) {
        this.parentSubgoalId = parentSubgoalId;
    }

    public boolean hasGoal()
    {
        return goalId > 0;
    }

    public boolean hasParentSubgoal()
    {
        return parentSubgoalId > 0;
    }
}