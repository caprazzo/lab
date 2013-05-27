package net.caprazzi.osgi.examples.hellorestimpl;

import com.factset.im.examples.hello.api.HelloWorldService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello-rest")
public class HelloWorldRestImpl {

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
