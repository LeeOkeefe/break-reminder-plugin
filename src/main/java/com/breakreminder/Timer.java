package com.breakreminder;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;

public class Timer
{
    private final Instant endTime;

    public Timer(Instant endTime)
    {
        this.endTime = endTime;

        if (endTime == null)
        {
            throw new NullPointerException("End time cannot be null");
        }
    }

    public Duration getDurationBetweenSystemClockAndEndTime()
    {
        Instant now = Instant.now();

        return Duration.between(now, endTime);
    }

    public String formatDuration(Duration duration, boolean showSeconds)
    {
        long hours = duration.toHours();

        String format;

        if (hours > 0)
        {
            format = showSeconds ? "H:mm:ss" : "H:mm";
        }
        else
        {
            format = showSeconds ? "mm:ss" : "mm";
        }

        long milliseconds = duration.abs().toMillis();

        return DurationFormatUtils.formatDuration(milliseconds, format, false);
    }

    public Duration roundMinutes(Duration duration)
    {
        if (duration.isNegative())
        {
            return duration;
        }

        return duration.plusMinutes(1);
    }
}
