package com.factset.im.examples.hellorestimpl;

import com.factset.im.examples.hello.api.HelloWorldServiceProvider;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello-rest")
@Component
public class HelloWorldRestImpl {

    @Reference
    private HttpService httpService;

    @Reference
    private HelloWorldServiceProvider.HelloWorldService helloService;

    @Activate
    public void start(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Start with httpService " + httpService + " and helloService " + helloService);
        ServletContainer jerseyServlet = new ServletContainer(new HelloWorldRestApplication(this));
        httpService.registerServlet("/teo", jerseyServlet, null, null);
    }

    @Deactivate
    public void stop(ComponentContext context) throws Exception {
        System.out.println("HelloWorldRestImplActivator Stop with impl " );
        httpService.unregister("/teo");
    }

    @Modified
    public void modified(ComponentContext context) throws Exception {
        System.out.println("HelloWorldRestImplActivator Modified with httpService " + httpService + " and helloService " + helloService);
    }

    @GET
    public String sayHello() {
        return helloService.sayHello();
    }

}
