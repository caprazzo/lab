package net.caprazzi.xmpp.botservice;

import net.caprazzi.xmpp.bot.BotEnvironment;
import net.caprazzi.xmpp.component.PacketProcessorExecutor;
import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.packet.Packet;

import java.util.concurrent.Executors;

public abstract class AbstractBotService {

    private final Logger Log = LoggerFactory.getLogger(AbstractBotService.class);

    public final void run(ServiceConfiguration configuration) {
        ServiceEnvironment environment = new ServiceEnvironment(configuration);
        run(environment);

        ExternalComponentManager manager = new ExternalComponentManager(configuration.getHost(), configuration.getPort());
        final ComponentPacketSender sender = new ComponentPacketSender(manager);

        PacketProcessorExecutor botExecutor = new PacketProcessorExecutor(Executors.newFixedThreadPool(10), sender);
        SubdomainPacketRouter router = new SubdomainPacketRouter(botExecutor);

        for(SubdomainEnvironment subdomain : environment.getSubdomains()) {

            final Component component = new PacketRoutingComponent(router, subdomain.getName());
            try {
                manager.setSecretKey(subdomain.getName(), subdomain.getSecret());
                manager.removeComponent(subdomain.getName());
                manager.addComponent(subdomain.getName(), component);
                Log.info("Connected");
            } catch (ComponentException e) {
                throw new RuntimeException(e);
            }

            BotEnvironment env = new BotEnvironment() {
                @Override
                public void send(Packet packet) {
                    sender.send(new ComponentPacket(component, packet));
                }
            };

            for(AnnotatedBotHolder holder : subdomain.getBots()) {
                AnnotatedBotPacketProcessor processor = new AnnotatedBotPacketProcessor(holder.getBot(), env);
                router.addBot(processor, subdomain.getName(), holder.getNodeFilter());
            }
        }

        sender.start();
    }

    protected abstract void run(ServiceEnvironment environment);
}
