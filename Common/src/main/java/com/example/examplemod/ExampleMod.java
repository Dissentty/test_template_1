package com.example.examplemod;

import com.example.examplemod.generated.ModConstants;
import com.example.examplemod.platform.Platform;
import com.example.examplemod.registry.RegistrationBridge;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class ExampleMod {
    public static final Logger LOGGER = LogUtils.getLogger();

    private ExampleMod() {}

    public static void init() {
        LOGGER.info("Loading {} {} on {}", ModConstants.MOD_NAME, ModConstants.VERSION, Platform.get().loaderName());
        RegistrationBridge.registerCommon();
    }
}
