package com.cyrildewit.pgc.domain.suggestive_action.analyzing;

import java.util.Optional;

import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;

public interface SuggestiveActionAnalyzer {
    public Optional<SuggestiveAction> analyze();
}