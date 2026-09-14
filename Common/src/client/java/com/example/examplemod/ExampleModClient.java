package com.example.examplemod;

import com.example.examplemod.registry.RegistrationBridge;
import net.minecraft.client.Minecraft;

public final class ExampleModClient {
    private ExampleModClient() {}

    public static void init() {
        ExampleMod.LOGGER.debug("Client bootstrap: {}", Minecraft.getInstance());
        RegistrationBridge.registerClient();
    }
}
