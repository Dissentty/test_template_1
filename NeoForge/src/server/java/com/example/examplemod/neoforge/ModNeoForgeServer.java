package com.example.examplemod.neoforge;

import com.example.examplemod.ExampleModServer;
import com.example.examplemod.generated.ModConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.DEDICATED_SERVER)
public final class ModNeoForgeServer {
    private ModNeoForgeServer() {}

    @SubscribeEvent
    public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
        ExampleModServer.init();
    }
}
