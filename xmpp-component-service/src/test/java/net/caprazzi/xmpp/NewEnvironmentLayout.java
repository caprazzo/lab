package net.caprazzi.xmpp;

import net.caprazzi.xmpp.bot.Receive;
import net.caprazzi.xmpp.botservice.AbstractBotService;
import net.caprazzi.xmpp.botservice.ServiceConfiguration;
import net.caprazzi.xmpp.botservice.ServiceEnvironment;
import net.caprazzi.xmpp.botservice.SubdomainEnvironment;
import org.xmpp.packet.Message;

public class NewEnvironmentLayout extends AbstractBotService {

    public static void main(String[] main) {
        ServiceConfiguration configuration = new ServiceConfiguration();
        configuration.setHost("localhost");
        configuration.setPort(5275);
        configuration.setSecret("secret");
        new NewEnvironmentLayout().run(configuration);
    }

    @Override
    public void run(ServiceEnvironment environment) {
        SubdomainEnvironment subdomain = environment.getSubdomain("foo");
        EchoBot bot = new EchoBot();
        subdomain.addBot(bot, "echo");

    }

    public static class EchoBot {

        @Receive
        public Message echo(Message msg) {
            Message reply = new Message();
            reply.setTo(msg.getFrom());
            reply.setFrom(msg.getTo());
            reply.setBody("You said xx: " + msg.getBody());
            return reply;
        }

    }

}
