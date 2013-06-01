package net.caprazzi.xmpp.botservice;

import com.google.common.collect.HashBasedTable;
import net.caprazzi.xmpp.component.PacketProcessor;
import net.caprazzi.xmpp.component.PacketProcessorExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

import java.util.Map;
import java.util.Set;

public class SubdomainPacketRouter {

    private final Logger Log = LoggerFactory.getLogger(SubdomainPacketRouter.class);

    private final PacketProcessorExecutor executor;
    private final HashBasedTable<PacketProcessor, String, NodeFilter> table = HashBasedTable.create();

    public SubdomainPacketRouter(PacketProcessorExecutor executor) {
        this.executor = executor;
    }

    public void route(Component component, String subdomain, Packet packet) {
        Log.debug("Request to route to subdomain {} for packet {}", subdomain, packet.toXML());
        for(Map.Entry<PacketProcessor, NodeFilter> entry : table.column(subdomain).entrySet()) {
            if (entry.getValue().accept(packet.getTo().getNode())) {
                Log.debug("Routing packet {} to component {}, processor {} ", packet.toXML(), component, entry.getKey());
                executor.execute(component, entry.getKey(), packet);
            }
        }
    }

    public synchronized void addBot(PacketProcessor bot, String subdomain, NodeFilter nodeFilter) {
        table.put(bot, subdomain, nodeFilter);
    }

    public synchronized void removeBot(PacketProcessor bot, String subdomain) {
        table.remove(bot, subdomain);
    }


    public synchronized Set<String> getDomains() {
        return table.columnKeySet();
    }


}
