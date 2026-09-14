package com.example.examplemod.fabric;

import com.example.examplemod.ExampleModServer;
import net.fabricmc.api.DedicatedServerModInitializer;

public final class ModFabricServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ExampleModServer.init();
    }
}
