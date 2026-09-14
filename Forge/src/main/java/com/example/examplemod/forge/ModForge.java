package com.example.examplemod.forge;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.generated.ModConstants;
import com.example.examplemod.platform.Platform;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModConstants.MOD_ID)
public final class ModForge {
    public ModForge(FMLJavaModLoadingContext context) {
        Platform.install(new ForgePlatformHelper());
        ExampleMod.init();
    }
}
