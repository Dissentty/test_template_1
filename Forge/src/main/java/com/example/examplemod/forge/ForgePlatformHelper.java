package com.example.examplemod.forge;

import com.example.examplemod.platform.PlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ForgePlatformHelper implements PlatformHelper {
    @Override public String loaderName() { return "Forge"; }
    @Override public boolean isModLoaded(String modId) { return ModList.get().isLoaded(modId); }
    @Override public boolean isDevelopmentEnvironment() { return !FMLEnvironment.production; }
    @Override public Path configDirectory() { return FMLPaths.CONFIGDIR.get(); }
}
