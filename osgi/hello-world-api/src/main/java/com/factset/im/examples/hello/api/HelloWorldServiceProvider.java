package com.factset.im.examples.hello.api;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

@Component
public class HelloWorldServiceProvider {

    public interface HelloWorldService {
        public String sayHello();
    }

    @Activate
    public void start(ComponentContext cc) throws Exception {
        System.out.println("HelloWorldApiActivator Start");
    }

    @Deactivate
    public void stop(ComponentContext context) throws Exception {
        System.out.println("HelloWorldApiActivator Stop");
    }
}
