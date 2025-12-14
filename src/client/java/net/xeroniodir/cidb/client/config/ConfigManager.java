package net.xeroniodir.cidb.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.xeroniodir.cidb.client.ModConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static ModConfig current = ModConfig.createDefault();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("cidb_config.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ModConfig get() {
        return current;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                current = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                current = ModConfig.createDefault();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(current, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reset() {
        current = ModConfig.createDefault();
        save();
    }
}