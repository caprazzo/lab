package com.factset.im.examples.hellorestimpl;

import com.factset.im.examples.hello.api.HelloWorldServiceProvider;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;

import javax.servlet.Servlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Hashtable;


@Path("/hello-rest")
@Component(immediate = true)
public class HelloWorldRestImpl {

    @Reference
    private HttpService http;

    @Activate
    public void start(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Start with httpService " + http );
        setHttpService(http, this.getClass().getName());
    }

    @Deactivate
    public void stop(ComponentContext context) throws Exception {
        System.out.println("HelloWorldServiceImplActivator Stop with impl " );
    }

    @GET
    public String sayHello() {
        return "hello fool";
    }

    public void setHttpService(HttpService httpService, String classNames) throws Exception {
        Hashtable<String, String> initParams =
                new Hashtable<String, String>();

        //initParams.put(
        //        "com.sun.ws.rest.config.property.resourceConfigClass",
        //        "com.factset.im.examples.hellorestimpl.OSGiResourceConfig");

        initParams.put("javax.ws.rs.Application", HelloWorldRestApplication.class.getName());

        Servlet jerseyServlet = new ServletContainer();
        httpService.registerServlet("/teo",
                jerseyServlet, initParams, null);
    }
}
