package net.caprazzi.xmpp.component.bot;

import net.caprazzi.xmpp.BotEnvironment;
import org.xmpp.packet.Packet;

public interface PacketProcessor {
    public void processPacket(Packet packet, BotEnvironment environment);
}

