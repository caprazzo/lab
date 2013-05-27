package net.caprazzi.xmpp.component;

import com.google.common.collect.HashBasedTable;
import net.caprazzi.xmpp.component.bot.Bot;
import net.caprazzi.xmpp.component.bot.BotExecutor;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

import java.util.Map;
import java.util.Set;

public class PacketRouter {

    private final BotExecutor executor;
    private final HashBasedTable<Bot, String, NodeFilter> table = HashBasedTable.create();

    public PacketRouter(BotExecutor executor) {
        this.executor = executor;
    }

    public void route(Component component, String subdomain, Packet packet) {
        for(Map.Entry<Bot, NodeFilter> entry : table.column(subdomain).entrySet()) {
            if (entry.getValue().accept(packet.getTo().getNode())) {
                executor.execute(component, entry.getKey(), packet);
            }
        }
    }

    public synchronized void addBot(Bot bot, String subdomain, NodeFilter nodeFilter) {
        table.put(bot, subdomain, nodeFilter);
    }

    public synchronized void removeBot(Bot bot, String subdomain) {
        table.remove(bot, subdomain);
    }


    public synchronized Set<String> getDomains() {
        return table.columnKeySet();
    }
}
