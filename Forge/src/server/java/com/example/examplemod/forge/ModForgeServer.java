package com.example.examplemod.forge;

import com.example.examplemod.ExampleModServer;
import com.example.examplemod.generated.ModConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.DEDICATED_SERVER)
public final class ModForgeServer {
    private ModForgeServer() {}

    @SubscribeEvent
    public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
        ExampleModServer.init();
    }
}
