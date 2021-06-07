package com.cyrildewit.pgc.logic.model;

public abstract class Model {
    public abstract long getId();

    public String getMorphClass() {
        return this.getClass().getSimpleName();
    }
}
