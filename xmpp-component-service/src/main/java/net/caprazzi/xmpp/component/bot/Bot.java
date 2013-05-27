package net.caprazzi.xmpp.component.bot;

import org.xmpp.packet.Message;

/**
 * Alternatively, it can be considered to provide
 * class and method-level annotations to simplify creation of bots
 * @Bot for class level
 * and @Reply for simple reactive bots
 */
public interface Bot {
    public BotResponse handleMessage(Message message);
}

