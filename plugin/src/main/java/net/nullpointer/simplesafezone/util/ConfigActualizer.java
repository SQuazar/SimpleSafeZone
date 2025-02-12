package net.nullpointer.simplesafezone.util;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigActualizer {
    private final Plugin plugin;

    public ConfigActualizer(Plugin plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration actualize(String resourcePath) throws IOException {
        Configuration resource = YamlConfiguration.loadConfiguration(
                new InputStreamReader(plugin.getResource(resourcePath))
        );
        YamlConfiguration original = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), resourcePath));
        Map<String, Object> missing = new HashMap<>();
        resource.getValues(true).forEach((key, value) -> {
            if (!original.contains(key)) missing.put(key, value);
        });
        missing.forEach(original::set);
        original.save(new File(plugin.getDataFolder(), resourcePath));

        return original;
    }
}
