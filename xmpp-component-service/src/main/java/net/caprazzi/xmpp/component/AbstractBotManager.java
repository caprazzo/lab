package net.caprazzi.xmpp.component;

import net.caprazzi.xmpp.component.bot.BotServiceManager;

import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBotManager {

    private final Logger Log;
    public AbstractBotManager() {
         Log = LoggerFactory.getLogger(this.getClass());
    }

    protected abstract void initialize(BotServiceManager botService);

    protected final void run(String args[]) {
        Log.info("Starting with args " + args.length);

        // TODO: parse basic options from yml (see https://github.com/codahale/dropwizard/tree/master/dropwizard-configuration)
        // TODO: don't start botservice until after initialization is completed

        final ExternalComponentManager manager = new ExternalComponentManager("localhost", 15275);
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