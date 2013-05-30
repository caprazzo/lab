package net.caprazzi.osgi.examples.hellorestimpl;



import net.caprazzi.osgi.examples.hello.api.HelloWorldService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/hello-rest")
public class HelloWorldRestImpl {

    @Context

    private final HelloWorldService helloService;

    public HelloWorldRestImpl(HelloWorldService helloService) {
        this.helloService = helloService;
        System.out.println("constructor of HelloWorldRestImpl (annots)");
    }

    @GET
    public String sayHello() {
        return helloService.sayHello();
    }

}
