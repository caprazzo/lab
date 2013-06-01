package net.caprazzi.xmpp.botservice;

import com.google.common.base.Optional;
import net.caprazzi.xmpp.bot.BotEnvironment;
import net.caprazzi.xmpp.component.PacketProcessor;
import org.xmpp.packet.Packet;

public class AnnotatedBotPacketProcessor implements PacketProcessor {
    private final AnnotatedBotObject bot;
    public AnnotatedBotPacketProcessor(AnnotatedBotObject bot, final BotEnvironment env) {
        this.bot = bot;
        bot.inject(env);
    }

    @Override
    public void processPacket(Packet packet, BotEnvironment environment) {
        Optional<Packet> response = bot.receive(packet);
        if (response.isPresent()) {
            environment.send(response.get());
        }
    }
}
