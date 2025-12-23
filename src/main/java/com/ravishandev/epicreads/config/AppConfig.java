package com.ravishandev.epicreads.config;

import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        packages("com.ravishandev.epicreads.controller");
    }
}
