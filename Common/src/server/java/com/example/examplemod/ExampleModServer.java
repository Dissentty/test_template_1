package com.example.examplemod;

import com.example.examplemod.registry.RegistrationBridge;

public final class ExampleModServer {
    private ExampleModServer() {}

    public static void init() {
        ExampleMod.LOGGER.debug("Dedicated-server bootstrap");
        RegistrationBridge.registerServer();
    }
}
