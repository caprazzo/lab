package net.caprazzi.xmpp.component;

import net.caprazzi.xmpp.bot.BotEnvironment;
import org.xmpp.packet.Packet;

public interface PacketProcessor {
    public void processPacket(Packet packet, BotEnvironment environment);
}

