package net.caprazzi.xmpp.bot;

import org.xmpp.packet.Packet;

public interface BotEnvironment {
    void send(Packet packet);
}
