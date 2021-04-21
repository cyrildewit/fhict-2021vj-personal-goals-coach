package com.cyrildewit.pcg.models;

import java.time.LocalDateTime;

public class SubGoal {
    protected int id;
    protected String uuid;
    protected String title;
    protected String description;
    protected LocalDateTime deadline;

    public SubGoal() {}

    public SubGoal(String uuid, String title, String description, LocalDateTime deadline) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public SubGoal(int id, String uuid, String title, String description, LocalDateTime deadline) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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
}