package net.caprazzi.xmpp.component;

import org.jivesoftware.whack.ExternalComponentManager;

public abstract class AbstractBotManager{

    protected abstract void initialize(BotServiceManager botService);

    protected final void run(String args[]) {
        // parse basic options from yml
        final ExternalComponentManager manager = new ExternalComponentManager("localhost", 5275);
        BotServiceManager botService = new BotServiceManager(manager);
        initialize(botService);
    }
}
