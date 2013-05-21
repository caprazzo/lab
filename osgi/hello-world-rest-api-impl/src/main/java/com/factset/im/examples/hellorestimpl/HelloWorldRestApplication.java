package com.factset.im.examples.hellorestimpl;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class HelloWorldRestApplication extends Application {

    public HelloWorldRestApplication() {
        System.out.println("constructor() of " + getClass().getName());
    }

    @Override
    public Set<Object> getSingletons() {
        System.out.println("getSingletons() of " + getClass().getName());
        HashSet<Object> set = new HashSet<Object>(super.getSingletons());
        set.add(new HelloWorldRestImpl());
        return set;
    }

}
