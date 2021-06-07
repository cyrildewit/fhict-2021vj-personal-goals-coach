package com.cyrildewit.pgc.domain;

public abstract class Model {
    public abstract long getId();

    public String getMorphClass() {
        return this.getClass().getSimpleName();
    }
}
