package com.factset.im.examples.helloimpl;


import com.factset.im.examples.hello.api.HelloWorldService;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.service.component.ComponentContext;

@Component
@Provides
public class HelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String sayHello() {
        return "HelloWorldServiceImpl says hello - version 6";
    }

    @Bind
    public void start(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Start with impl");
    }

    @Unbind
    public void stop(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Stop with impl");
    }
}
