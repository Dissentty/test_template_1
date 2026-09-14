package com.example.examplemod.registry;

/**
 * Small extension point for future blocks/items/commands/events.
 * Keep vanilla/shared descriptions in Common and loader-specific registration glue in loader modules.
 */
public final class RegistrationBridge {
    private RegistrationBridge() {}

    public static void registerCommon() {
        // Blocks / items / commands / common events.
    }

    public static void registerClient() {
        // Key bindings / renderers / client events.
    }

    public static void registerServer() {
        // Dedicated-server hooks.
    }
}
