package com.breakreminder;

import com.google.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.time.Duration;

public class BreakOverlay extends Overlay
{
    private final PanelComponent panelComponent = new PanelComponent();
    private final BreakReminderPlugin plugin;
    private final BreakConfig config;

    @Inject
    private BreakOverlay(BreakReminderPlugin plugin, BreakConfig config)
    {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Timer timer = plugin.getTimer();

        Duration timeRemaining = timer.getDurationBetweenSystemClockAndEndTime();

        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(createTitle(timeRemaining));
        panelComponent.getChildren().add(createTimeDisplay(timeRemaining, timer));

        if (plugin.getCurrentBreakIntervalLength() != config.nextBreakInterval())
        {
            LayoutableRenderableEntity nextInterval = createNextInterval(timer);

            panelComponent.getChildren().add(nextInterval);
        }

        panelComponent.setPreferredSize(new Dimension(105, 0));
        panelComponent.setBorder(new Rectangle(5, 5, 5, 5));

        setPosition(config.overlayPosition());

        return panelComponent.render(graphics);
    }

    private LayoutableRenderableEntity createTitle(Duration timeRemaining)
    {
        return timeRemaining.isNegative()
            ? buildTitle("Take a break", Color.red)
            : buildTitle("Next break:", Color.green);
    }

    private LayoutableRenderableEntity createTimeDisplay(Duration timeRemaining, Timer timer)
    {
        String formattedTime = timer.formatDuration(config.DisplaySeconds()
                ? timeRemaining
                : timer.roundMinutes(timeRemaining), config.DisplaySeconds());

        return timeRemaining.isNegative()
                ? buildLine("Overdue:", Color.orange, formattedTime, Color.orange)
                : buildLine("Current:", Color.white, formattedTime, Color.white);
    }

    private LayoutableRenderableEntity createNextInterval(Timer timer)
    {
        Color color = SystemColor.info;
        Duration minutes = Duration.ofMinutes(config.nextBreakInterval());
        String duration = timer.formatDuration(minutes, config.DisplaySeconds());

        return buildLine("Next:", color, duration, color);
    }

    private TitleComponent buildTitle(String text, Color color)
    {
        return TitleComponent.builder()
                .text(text)
                .color(color)
                .build();
    }

    private LineComponent buildLine(String left, Color leftColor, String right, Color rightColor)
    {
        return LineComponent
                .builder()
                .left(left).leftColor(leftColor)
                .right(right).rightColor(rightColor)
                .build();
    }
}
