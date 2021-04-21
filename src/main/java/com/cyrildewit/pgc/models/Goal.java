package com.cyrildewit.pgc.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Goal {
    protected int id;
    protected UUID uuid;
    protected String title;
    protected String description;
    protected LocalDateTime deadline;

    public Goal() {}

    public Goal(UUID uuid, String title, String description, LocalDateTime deadline) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public Goal(int id, UUID uuid, String title, String description, LocalDateTime deadline) {
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
}