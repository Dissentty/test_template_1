package com.example.examplemod.platform;

import java.util.Objects;

public final class Platform {
    private static volatile PlatformHelper helper;

    private Platform() {}

    public static void install(PlatformHelper implementation) {
        Objects.requireNonNull(implementation, "implementation");
        if (helper != null) {
            throw new IllegalStateException("Platform helper is already installed: " + helper.loaderName());
        }
        helper = implementation;
    }

    public static PlatformHelper get() {
        PlatformHelper current = helper;
        if (current == null) {
            throw new IllegalStateException("Platform helper has not been installed by a loader entrypoint yet.");
        }
        return current;
    }
}
