package com.example.examplemod.fabric;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.platform.Platform;
import net.fabricmc.api.ModInitializer;

public final class ModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Platform.install(new FabricPlatformHelper());
        ExampleMod.init();
    }
}
