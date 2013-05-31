package net.caprazzi.xmpp;

import net.caprazzi.xmpp.botservice.AbstractBotService;
import net.caprazzi.xmpp.botservice.ServiceConfiguration;
import net.caprazzi.xmpp.botservice.ServiceEnvironment;
import net.caprazzi.xmpp.botservice.SubdomainEnvironment;

public class NewEnvironmentLayout extends AbstractBotService {

    public static void main(String[] main) {
        ServiceConfiguration configuration = null;
        new NewEnvironmentLayout().run(configuration);
    }

    @Override
    public void run(ServiceEnvironment environment) {
        SubdomainEnvironment subdomain = environment.getSubdomain("foo");
        subdomain.addBot(null, null);
    }

}
