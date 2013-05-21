package com.factset.im.examples.helloimpl;

import com.factset.im.examples.hello.api.HelloWorldServiceProvider.HelloWorldService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

@Component
@Service(HelloWorldService.class)
public class HelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String sayHello() {
        return "HelloWorldServiceImpl says hello - version 6";
    }

    @Activate
    public void start(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Start with impl");
    }

    @Deactivate
    public void stop(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Stop with impl");
    }
}
