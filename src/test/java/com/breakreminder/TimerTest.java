package com.breakreminder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class TimerTest
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test()
    public void startTimerThrowsExceptionWhenEndTimeIsNull()
    {
        exception.expect(NullPointerException.class);
        exception.expectMessage("End time cannot be null");

        new Timer(null);
    }

    @Test
    public void getDurationBetweenSystemClockAndEndTimeReturnsExpectedDuration()
    {
        final int seconds = 123;

        Instant endTime = Instant.now().plusSeconds(seconds);

        Timer timer = new Timer(endTime);

        Duration duration = timer.getDurationBetweenSystemClockAndEndTime();
        Duration expectedDuration = Duration.between(Instant.now(), endTime);

        assert duration.toMinutes() == expectedDuration.toMinutes();
        assert duration.getSeconds() == expectedDuration.getSeconds();
    }

    @Test
    public void formatDurationReturnsExpectedMinutesAndSeconds()
    {
        final int seconds = 123;

        Instant endTime = Instant.now().plusSeconds(seconds);

        Timer timer = new Timer(endTime);

        Duration duration = timer.getDurationBetweenSystemClockAndEndTime();

        String minutesAndSeconds = timer.formatDuration(duration, true);
        String minutes = timer.formatDuration(duration, false);

        assert Objects.equals(minutesAndSeconds, "2:2");
        assert Objects.equals(minutes, "3");
    }

    @Test
    public void formatDurationRoundsMinutesUpWhenDisplaySecondsFalseAndDurationIsPositive()
    {
        final int seconds = 61;
        final boolean displaySeconds = false;

        Instant endTime = Instant.now().plusSeconds(seconds);

        Timer timer = new Timer(endTime);

        Duration duration = timer.getDurationBetweenSystemClockAndEndTime();

        String timeFormatted = timer.formatDuration(duration, displaySeconds);

        assert timeFormatted.equals("2");
    }

    @Test
    public void formatDurationRoundsMinutesDownWhenDisplaySecondsFalseAndDurationIsNegative()
    {
        final int seconds = 61;
        final boolean displaySeconds = false;

        Instant instant = Instant.now().minusSeconds(seconds);

        Timer timer = new Timer(instant);

        Duration duration = timer.getDurationBetweenSystemClockAndEndTime();

        String timeFormatted = timer.formatDuration(duration, displaySeconds);

        assert timeFormatted.equals("1");
    }
}
