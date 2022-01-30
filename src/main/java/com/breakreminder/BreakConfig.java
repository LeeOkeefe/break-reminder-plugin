package com.breakreminder;

import net.runelite.client.config.*;
import net.runelite.client.ui.overlay.OverlayPosition;

@ConfigGroup("break")
public interface BreakConfig extends Config {
    @ConfigItem(
            keyName = "breakInterval",
            name = "Break interval",
            description = "Set the duration of the break reminder"
    )
    @Range(min = 15, max = 90)
    @Units(Units.MINUTES)
    default int nextBreakInterval() { return 45; }

    @ConfigItem(
            keyName = "timeFormat",
            name = "Display seconds",
            description = "Enable / disable seconds displayed on the timer"
    )
    default boolean DisplaySeconds() { return true; }

    @ConfigItem(
            keyName = "breakOverlayPosition",
            name = "Position",
            description = "Overlay position on the screen"
    )
    default OverlayPosition overlayPosition() { return OverlayPosition.ABOVE_CHATBOX_RIGHT; }
}
