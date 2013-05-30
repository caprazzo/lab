package net.caprazzi.xmpp.component.bot;

import net.caprazzi.xmpp.BotEnvironment;

public abstract class AbstractBot implements PacketProcessor {
    private final BotEnvironment environment;

    public AbstractBot(BotEnvironment environment) {
        this.environment = environment;
    }
}
