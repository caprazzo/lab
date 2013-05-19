package com.factset.im.examples.hellorestimpl;

/*
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
*

/**
 * Created with IntelliJ IDEA.
 * User: caprazzo
 * Date: 19/05/2013
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */

public class Activator { /*(implements BundleActivator {
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        System.out.println("Start Me Up");

        ServiceTracker httpServiceTracker = new ServiceTracker(bundleContext, HttpService.class.getName(), new ServiceTrackerCustomizer() {
            @Override
            public Object addingService(ServiceReference serviceReference) {
                Object service = bundleContext.getService(serviceReference);
                System.out.println("Obtained service reference " + service);
                return service;
            }

            @Override
            public void modifiedService(ServiceReference serviceReference, Object o) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void removedService(ServiceReference serviceReference, Object o) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        httpServiceTracker.open();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Turn Me Down");
    }
    */
}
