package com.example.examplemod.platform;

import java.nio.file.Path;

public interface PlatformHelper {
    String loaderName();
    boolean isModLoaded(String modId);
    boolean isDevelopmentEnvironment();
    Path configDirectory();
}
