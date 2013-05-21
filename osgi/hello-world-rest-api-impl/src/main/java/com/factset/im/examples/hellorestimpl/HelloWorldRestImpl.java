package com.factset.im.examples.hellorestimpl;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello-rest")
@Component(immediate = true)

public class HelloWorldRestImpl {

    @Reference
    private HttpService httpService;

    @Activate
    public void start(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Start with httpService " + httpService );
        ServletContainer jerseyServlet = new ServletContainer(new HelloWorldRestApplication());
        httpService.registerServlet("/teo", jerseyServlet, null, null);
    }

    @Deactivate
    public void stop(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Stop with impl " );
        httpService.unregister("/teo");
    }

    @GET
    public String sayHello() {
        System.out.println("Inside HelloWorldServiceImpl.sayHello");
        return "hello fool 35";
    }

}
