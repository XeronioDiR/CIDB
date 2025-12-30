package net.xeroniodir.cidb.client;
import net.fabricmc.api.ClientModInitializer;
import net.xeroniodir.cidb.client.config.ConfigManager;

import java.util.logging.Logger;

public class CidbClient implements ClientModInitializer {

    public static Logger LOGGER = Logger.getGlobal(); /// if needed to check some parameters or other stuff
    @Override
    public void onInitializeClient() {
        ConfigManager.load();
    }
}
