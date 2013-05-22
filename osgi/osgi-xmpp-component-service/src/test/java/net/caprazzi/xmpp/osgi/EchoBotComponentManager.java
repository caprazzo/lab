package net.caprazzi.xmpp.osgi;

import net.caprazzi.xmpp.component.*;
import org.xmpp.packet.Message;

public class EchoBotComponentManager extends AbstractBotManager {

    public static void main(String[] args) {
        new EchoBotComponentManager().run(args);
    }

    @Override
    protected void initialize(BotServiceManager botService) {
        botService.addDomain("myDomain", "secret");
        botService.addBot(new Bot() {
            @Override
            public BotResponse handleMessage(Message message) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, "myDomain", NodeFilters.singleNode("echo"));
    }
}
