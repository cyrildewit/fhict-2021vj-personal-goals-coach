package com.cyrildewit.pgc.application.view_model.front.goal.show;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.cyrildewit.pgc.domain.goal.model.Goal;

public final class GoalShowDto {
    private long id;
    private UUID uuid;
    private String title;
    private String description;
    private String formattedDeadline;
    private String subgoalsCountFormatted;
    private String suggestiveActionsCountFormatted;

    public GoalShowDto(long id, UUID uuid, String title, String description, String formattedDeadline) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.formattedDeadline = formattedDeadline;
    }

    public GoalShowDto(long id, UUID uuid, String title, String description, String formattedDeadline, String subgoalsCountFormatted, String suggestiveActionsCountFormatted) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.subgoalsCountFormatted = subgoalsCountFormatted;
        this.suggestiveActionsCountFormatted = suggestiveActionsCountFormatted;
    }

    public static GoalShowDto fromGoalEntity(Goal goal) {
        DateTimeFormatter goalDeadlineDateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        return new GoalShowDto(
                goal.getId(),
                goal.getUuid(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getDeadline().format(goalDeadlineDateTimeFormatter)
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

    public String getSubgoalsCountFormatted() {
        return subgoalsCountFormatted;
    }

    public void setSubgoalsCountFormatted(String subgoalsCountFormatted) {
        this.subgoalsCountFormatted = subgoalsCountFormatted;
    }

    public String getSuggestiveActionsCountFormatted() {
        return suggestiveActionsCountFormatted;
    }

    public void setSuggestiveActionsCountFormatted(String suggestiveActionsCountFormatted) {
        this.suggestiveActionsCountFormatted = suggestiveActionsCountFormatted;
    }
}