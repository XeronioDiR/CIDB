package net.xeroniodir.cidb.client;

import net.fabricmc.api.ClientModInitializer;

import java.util.logging.Logger;

public class CidbClient implements ClientModInitializer {

    public static Logger LOGGER = Logger.getGlobal();
    @Override
    public void onInitializeClient() {
        ModConfig.HANDLER.load();
    }
}
