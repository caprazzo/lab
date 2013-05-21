package com.factset.im.examples.hellorestimpl;

import com.factset.im.examples.hello.api.HelloWorldService;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;

@Path("/hello-rest")
//@Component(name = "HelloWorldRestImpl", architecture = true)
//@Instantiate(name = "HelloWorldRestImplInstance")
public class HelloWorldRestImpl {

    public HelloWorldRestImpl() {
        System.out.println("constructor of HelloWorldRestImpl");
    }

    //@Requires
    private HttpService httpService = null;

    private String m_name;

    @Requires
    private HelloWorldService helloService;

    @Validate
    public void starting() {
        System.out.println("HelloWorldServiceImplActivator Start with httpService " + httpService + " and helloService " + helloService);
        ServletContainer jerseyServlet = new ServletContainer(new HelloWorldRestApplication(this));
        //httpService.registerServlet("/teo", jerseyServlet, null, null);
    }

    @Invalidate
    public void stopping() {
        System.out.println("HelloWorldRestImplActivator Stop with impl " );
        //httpService.unregister("/teo");
    }

    @Updated
    public void updated(HashMap map) {
        System.out.println("updated");
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
