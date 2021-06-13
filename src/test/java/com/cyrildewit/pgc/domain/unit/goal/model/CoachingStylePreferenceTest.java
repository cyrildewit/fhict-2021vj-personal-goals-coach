package com.cyrildewit.pgc.domain.goal.model;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;

class CoachingStylePreferenceTest {
    @Test
    void getSuggestDeleteGoalBeforePeriodDatetimeReturnsLocalDateTimeObjects() {
        long suggestDeleteGoalBeforePeriod = 3600;
        CoachingStylePreference coachingStylePreference = new CoachingStylePreference();
        coachingStylePreference.setSuggestDeleteGoalBeforePeriod(suggestDeleteGoalBeforePeriod);

        assertTrue(coachingStylePreference.getSuggestDeleteGoalBeforePeriodDatetime() instanceof LocalDateTime);
    }

    @Test
    void getSuggestPinGoalBeforePeriodDatetimeReturnsLocalDateTimeObjects() {
        long suggestPinGoalBeforePeriod = 3600;
        CoachingStylePreference coachingStylePreference = new CoachingStylePreference();
        coachingStylePreference.setSuggestPinGoalBasedOnActivityStartDateTime(suggestPinGoalBeforePeriod);

        assertTrue(coachingStylePreference.getSuggestPinGoalBasedOnActivityBeforePeriodDatetime() instanceof LocalDateTime);
    }
}
