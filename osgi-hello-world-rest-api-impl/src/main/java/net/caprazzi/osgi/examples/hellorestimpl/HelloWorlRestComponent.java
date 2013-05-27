package net.caprazzi.osgi.examples.hellorestimpl;

import com.factset.im.examples.hello.api.HelloWorldService;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.service.http.HttpService;

import java.util.Dictionary;

@Component(immediate = true)
@Instantiate
public class HelloWorlRestComponent {

    @Requires
    private HttpService httpService;

    @org.apache.felix.ipojo.handler.temporal.Requires
    private HelloWorldService helloService;

    @Validate
    public void starting() throws Exception {
        System.out.println("HelloWorldServiceImplActivator Start with httpService " + httpService + " and helloService " + helloService);
        ServletContainer jerseyServlet = new ServletContainer(new net.caprazzi.osgi.examples.hellorestimpl.HelloWorldRestApplication(new HelloWorldRestImpl(helloService)));
        httpService.registerServlet("/teo", jerseyServlet, null, null);
        System.out.println("Servlet registered");
    }

    @Invalidate
    public void stopping() {
        System.out.println("HelloWorldRestImplActivator Stop with impl " );
        httpService.unregister("/teo");
    }

    @Updated
    public void updated(Dictionary map) {
        System.out.println("updated");
    }

    //@PostRegistration
    //public void registered(ServiceReference ref) {
    //    System.out.println("Registered " + ref);
    //}

    //@PostUnregistration
    //public void unregistered(ServiceReference ref) {
    //    System.out.println("Unregistered " + ref);
    //}

   //@Modified
   //public void modified() throws Exception {
   //    System.out.println("HelloWorldRestImplActivator Modified with httpService " + httpService + " and helloService " + helloService);
   //}
}
