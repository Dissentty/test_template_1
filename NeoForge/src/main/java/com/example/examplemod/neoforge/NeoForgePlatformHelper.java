package com.example.examplemod.neoforge;

import com.example.examplemod.platform.PlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoForgePlatformHelper implements PlatformHelper {
    @Override public String loaderName() { return "NeoForge"; }
    @Override public boolean isModLoaded(String modId) { return ModList.get().isLoaded(modId); }
    @Override public boolean isDevelopmentEnvironment() { return !FMLEnvironment.isProduction(); }
    @Override public Path configDirectory() { return FMLPaths.CONFIGDIR.get(); }
}
