package net.caprazzi.xmpp.osgi;

import net.caprazzi.xmpp.component.*;
import net.caprazzi.xmpp.component.bot.Bot;
import net.caprazzi.xmpp.component.bot.BotResponse;
import net.caprazzi.xmpp.component.bot.BotResponses;
import net.caprazzi.xmpp.component.bot.BotServiceManager;
import org.xmpp.packet.Message;

public class EchoBotComponentManager extends AbstractBotManager {

    public static void main(String[] args) {
        new EchoBotComponentManager().run(args);
    }

    @Override
    protected void initialize(BotServiceManager botService) {
        botService.addDomain("foo", "secret");

        botService.addBot(new Bot() {
            @Override
            public BotResponse handleMessage(Message message) {
                Message reply = new Message();
                reply.setTo(message.getFrom());
                reply.setFrom(message.getTo());
                reply.setBody("You said: " + message.getBody());
                return BotResponses.from(reply);
            }
        }, "foo", NodeFilters.singleNode("echo"));
    }
}
