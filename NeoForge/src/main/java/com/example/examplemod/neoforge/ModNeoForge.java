package com.example.examplemod.neoforge;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.generated.ModConstants;
import com.example.examplemod.platform.Platform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(ModConstants.MOD_ID)
public final class ModNeoForge {
    public ModNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Platform.install(new NeoForgePlatformHelper());
        ExampleMod.init();
    }
}
