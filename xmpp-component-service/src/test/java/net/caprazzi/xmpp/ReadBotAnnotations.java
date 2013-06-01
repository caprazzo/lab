package net.caprazzi.xmpp;


import com.google.common.base.Optional;
import net.caprazzi.xmpp.component.AbstractBotManager;
import net.caprazzi.xmpp.component.AnnotatedBotObject;
import net.caprazzi.xmpp.component.AnnotatedBotPacketProcessor;
import net.caprazzi.xmpp.component.NodeFilters;
import net.caprazzi.xmpp.component.bot.BotServiceManager;

import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

public class ReadBotAnnotations extends AbstractBotManager {

    public static void main(String[] args) {
        new ReadBotAnnotations().run(args);
    }

    @Override
    protected void initialize(BotServiceManager botService) {
        botService.addDomain("foo", "secret");
        final AnnotatedBot bot = new AnnotatedBot();
        Optional<AnnotatedBotObject> annotatedBot = AnnotatedBotObject.from(bot);
        if (annotatedBot.isPresent()) {
            // TODO: use botService.addBot()
            //AnnotatedBotPacketProcessor processor = new AnnotatedBotPacketProcessor(annotatedBot.get(), sender);
            //botService.addBot(processor, "foo", NodeFilters.singleNode("foo"));
        }
    }

    public static class AnnotatedBot {

        @BotContext private BotEnvironment botContext;

        /*
        @Receive
        public Message receive(Message receive) {
            Message reply = new Message();
            reply.setTo(receive.getFrom());
            reply.setFrom(receive.getTo());
            reply.setBody("You said: " + receive.getBody());
            return reply;
        }

        @Receive
        public Message receive2(Message receive) {
            Message reply = new Message();
            reply.setTo(receive.getFrom());
            reply.setFrom(receive.getTo());
            reply.setBody("You said2: " + receive.getBody());
            return reply;
        }
        */

        @Receive
        public Message receive3(Packet receive) {
            Message reply = new Message();
            reply.setTo(receive.getFrom());
            reply.setFrom(receive.getTo());
            reply.setBody("You said3: " + "packet");

            System.out.println(botContext);

            return reply;
        }

    }

}
