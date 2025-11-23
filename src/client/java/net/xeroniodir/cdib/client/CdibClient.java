package net.xeroniodir.cdib.client;

import net.fabricmc.api.ClientModInitializer;

public class CdibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.HANDLER.load();
    }
}
