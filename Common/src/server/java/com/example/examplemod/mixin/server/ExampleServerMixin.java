package com.example.examplemod.mixin.server;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DedicatedServer.class)
public abstract class ExampleServerMixin {
}
