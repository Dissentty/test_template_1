package com.example.examplemod.fabric;

import com.example.examplemod.ExampleModClient;
import net.fabricmc.api.ClientModInitializer;

public final class ModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExampleModClient.init();
    }
}
