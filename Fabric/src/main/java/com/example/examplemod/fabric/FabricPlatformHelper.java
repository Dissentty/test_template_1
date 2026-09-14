package com.example.examplemod.fabric;

import com.example.examplemod.platform.PlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class FabricPlatformHelper implements PlatformHelper {
    @Override public String loaderName() { return "Fabric"; }
    @Override public boolean isModLoaded(String modId) { return FabricLoader.getInstance().isModLoaded(modId); }
    @Override public boolean isDevelopmentEnvironment() { return FabricLoader.getInstance().isDevelopmentEnvironment(); }
    @Override public Path configDirectory() { return FabricLoader.getInstance().getConfigDir(); }
}
