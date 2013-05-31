package net.caprazzi.xmpp.botservice;

import net.caprazzi.xmpp.botservice.ServiceEnvironment;
import org.jivesoftware.whack.ExternalComponentManager;

public abstract class AbstractBotService {

    public final void run(ServiceConfiguration configuration) {
        ServiceEnvironment environment = new ServiceEnvironment(configuration);
        run(environment);
        ExternalComponentManager manager = new ExternalComponentManager(configuration.getHost(), configuration.getPort());
        environment.connect(manager);
    }

    protected abstract void run(ServiceEnvironment environment);
}
