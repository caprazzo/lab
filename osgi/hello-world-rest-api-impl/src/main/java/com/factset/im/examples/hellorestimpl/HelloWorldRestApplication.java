package com.factset.im.examples.hellorestimpl;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class HelloWorldRestApplication extends Application {

    public HelloWorldRestApplication() {
        System.out.println("constructor() of " + getClass().getName());
    }

    @Override
    public Set<Class<?>> getClasses() {
        System.out.println("getClasses() of " + getClass().getName());
        HashSet<Class<?>> set = new HashSet<Class<?>>(super.getClasses());
        set.add(HelloWorldRestImpl.class);
        return set;
    }
}
