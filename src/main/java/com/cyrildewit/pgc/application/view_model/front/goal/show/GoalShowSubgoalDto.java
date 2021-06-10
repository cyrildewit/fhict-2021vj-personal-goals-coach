package com.cyrildewit.pgc.application.view_model.front.goal.index;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.cyrildewit.pgc.domain.goal.model.Subgoal;

public final class GoalShowSubgoalDto {
    private long id;
    private UUID uuid;
    private String title;
    private String description;
    private String formattedDeadline;

    public GoalShowSubgoalDto(long id, UUID uuid, String title, String description, String formattedDeadline) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.formattedDeadline = formattedDeadline;
    }

    public static GoalShowSubgoalDto fromSubgoalEntity(Subgoal subgoal) {
        DateTimeFormatter goalDeadlineDateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        return new GoalShowSubgoalDto(
                subgoal.getId(),
                subgoal.getUuid(),
                subgoal.getTitle(),
                subgoal.getDescription(),
                subgoal.getDeadline().format(goalDeadlineDateTimeFormatter)
        );
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

    public String getFormattedDeadline() {
        return formattedDeadline;
    }

    public void setFormattedDeadline(String formattedDeadline) {
        this.formattedDeadline = formattedDeadline;
    }
}