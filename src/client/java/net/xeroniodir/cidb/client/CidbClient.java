package net.xeroniodir.cidb.client;

import net.fabricmc.api.ClientModInitializer;

public class CidbClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.HANDLER.load();
    }
}
