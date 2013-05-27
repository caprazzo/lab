package net.caprazzi.xmpp.component;

import org.xmpp.packet.Message;

public interface Bot {
    public BotResponse handleMessage(Message message);
}

