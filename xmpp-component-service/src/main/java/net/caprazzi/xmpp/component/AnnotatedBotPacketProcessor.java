package net.caprazzi.xmpp.component;

import com.google.common.base.Optional;
import net.caprazzi.xmpp.BotEnvironment;
import net.caprazzi.xmpp.component.AnnotatedBotObject;
import net.caprazzi.xmpp.component.bot.BotResponses;
import net.caprazzi.xmpp.component.bot.PacketProcessor;
import net.caprazzi.xmpp.component.bot.ResponsePacket;
import org.xmpp.packet.Packet;

/**
* Created with IntelliJ IDEA.
* User: mcaprari
* Date: 30/05/13
* Time: 18:10
* To change this template use File | Settings | File Templates.
*/
public class AnnotatedBotPacketProcessor implements PacketProcessor {
    private final AnnotatedBotObject bot;

    public AnnotatedBotPacketProcessor(AnnotatedBotObject bot) {
        this.bot = bot;
        bot.inject(new BotEnvironment() {

            @Override
            public void send(Packet packet) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Override
    public void processPacket(Packet packet, BotEnvironment environment) {
        Optional<Packet> response = bot.receive(packet);
        if (response.isPresent()) {
            environment.send(response.get());
        }
    }
}
