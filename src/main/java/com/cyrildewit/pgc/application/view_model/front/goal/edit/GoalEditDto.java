package com.cyrildewit.pgc.application.view_model.front.goal.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.cyrildewit.pgc.domain.goal.model.Goal;

public final class GoalEditDto {
    private UUID uuid;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public GoalEditDto(UUID uuid, String title, String description, LocalDateTime deadline) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public static GoalEditDto fromGoalEntity(Goal goal) {
//        DateTimeFormatter inputLocalDateTimeFormat = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss");

        return new GoalEditDto(
                goal.getUuid(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getDeadline()
        );
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

//    public String getDeadline() {
//        return deadline;
//    }
//
//    public void setDeadline(String deadline) {
//        this.deadline = deadline;
//    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
