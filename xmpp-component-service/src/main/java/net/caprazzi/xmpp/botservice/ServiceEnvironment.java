package net.caprazzi.xmpp.botservice;

import net.caprazzi.xmpp.component.PacketRouter;
import net.caprazzi.xmpp.component.Responder;
import net.caprazzi.xmpp.component.bot.BotExecutor;
import org.jivesoftware.whack.ExternalComponentManager;

import java.util.HashMap;
import java.util.concurrent.Executors;

public class ServiceEnvironment {

    private final HashMap<String, SubdomainEnvironment> subdomains = new HashMap<String, SubdomainEnvironment>();
    private final ServiceConfiguration configuration;
    private String secret;

    ServiceEnvironment(ServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    public SubdomainEnvironment getSubdomain(String subdomain) {
        SubdomainEnvironment env = subdomains.get(subdomain);
        if (env == null) {
            env = new SubdomainEnvironment(subdomain);
            env.setSecret(configuration.getSecret(subdomain));
            subdomains.put(subdomain, env);
        }
        return env;
    }

    public void connect(ExternalComponentManager componentManager) {
        Responder responder = new Responder(componentManager);
        BotExecutor botExecutor = new BotExecutor(Executors.newFixedThreadPool(10), responder);
        PacketRouter router = new PacketRouter(botExecutor);

        for(SubdomainEnvironment subdomain : subdomains.values()) {
            subdomain.connect(componentManager, router);
        }

        responder.start();
    }
}
