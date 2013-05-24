package net.caprazzi.xmpp.component;

import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class AbstractBotManager {

    private final Logger Log = LoggerFactory.getLogger(AbstractBotManager.class);

    protected abstract void initialize(BotServiceManager botService);

    protected final void run(String args[]) {
        Log.info("Starting with args " + args.length);

        // TODO: parse basic options from yml (see https://github.com/codahale/dropwizard/tree/master/dropwizard-configuration)
        // TODO: don't start botservice until after initialization is completed

        final ExternalComponentManager manager = new ExternalComponentManager("localhost", 5275);
        final BotServiceManager botService = new BotServiceManager(manager);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run()  {
                Log.info("Shutting down");
                botService.shutdown();
            }
        });

        initialize(botService);
    }
}
