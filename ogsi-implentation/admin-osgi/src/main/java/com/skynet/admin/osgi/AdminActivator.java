package com.skynet.admin.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class AdminActivator implements BundleActivator {
    public void start(BundleContext context) {
        System.out.println("🚀 OSGi: Skynet Admin Bundle Started and Registered.");
    }
    public void stop(BundleContext context) {
        System.out.println("🛑 OSGi: Skynet Admin Bundle Stopped.");
    }
}