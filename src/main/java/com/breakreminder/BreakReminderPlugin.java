package com.breakreminder;

import com.google.inject.Provides;
import lombok.Getter;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PluginDescriptor(
        name = "Break Reminder",
        description = "Reminder to take regular breaks"
)
public class BreakReminderPlugin extends Plugin {

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private BreakOverlay breakOverlay;

    @Inject
    private BreakConfig config;

    @Provides
    BreakConfig provideConfig(ConfigManager configManager) { return configManager.getConfig(BreakConfig.class); }

    @Getter
    private Timer timer;

    @Getter
    private int currentBreakIntervalLength;

    private final List<GameState> previousGameStates = new ArrayList<>();

    private final int hoppingStatesQuantity = 3;

    @Override
    protected void startUp()
    {
        currentBreakIntervalLength = config.nextBreakInterval();

        long intervalInSeconds = currentBreakIntervalLength * 60L;
        Instant intervalEndTime = Instant.now().plusSeconds(intervalInSeconds);

        timer = new Timer(intervalEndTime);
        overlayManager.add(breakOverlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(breakOverlay);
        previousGameStates.clear();
        timer = null;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        previousGameStates.add(event.getGameState());

        if (previousGameStates.size() < hoppingStatesQuantity)
        {
            return;
        }

        if (event.getGameState() == GameState.LOGGED_IN && !isWorldHopping())
        {
            currentBreakIntervalLength = config.nextBreakInterval();

            int intervalInSeconds = config.nextBreakInterval() * 60;
            Instant intervalEndTime = Instant.now().plusSeconds(intervalInSeconds);
            
            timer = new Timer(intervalEndTime);
        }
    }

    private boolean isWorldHopping()
    {
        int size = previousGameStates.size();
        List<GameState> states = previousGameStates.subList(size - hoppingStatesQuantity, size);

        return     states.get(0) == GameState.HOPPING
                && states.get(1) == GameState.LOADING
                && states.get(2) == GameState.LOGGED_IN;
    }
}
