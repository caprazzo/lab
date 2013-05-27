package net.caprazzi.xmpp.component.bot;


import net.caprazzi.xmpp.component.NodeFilter;
import net.caprazzi.xmpp.component.bot.Bot;
import org.xmpp.component.ComponentException;

public interface BotService {
    public void addBot(Bot bot, String subdomain, NodeFilter nodeFilter);
    public void removeBot(Bot bot, String subdomain);
    public void addDomain(String domain, String secret) throws ComponentException;
    public void removeDomain(String domain) throws ComponentException;

    void shutdown();
}
