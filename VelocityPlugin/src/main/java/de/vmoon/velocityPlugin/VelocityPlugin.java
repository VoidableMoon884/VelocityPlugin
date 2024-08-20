package de.vmoon.velocityPlugin;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.vmoon.velocityPlugin.Listeners.ConnectionListener;
import de.vmoon.velocityPlugin.commands.FreeHasCommand;
import de.vmoon.velocityPlugin.commands.KickallCommand;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin
        (
        id = "velocityplugin",
        name = "VelocityPlugin",
        version = "1.7",
        description = "A Velocity Plugin by VoidableMoon",
        url = "velocity.vmoon.de",
        authors = {"VoidableMoon884"}
)

public class VelocityPlugin {

    private final Logger logger;
    private final ProxyServer proxyServer;
    private List<String> serverNames;

    @Inject
    public VelocityPlugin(Logger logger, ProxyServer proxyServer1) {
        this.logger = logger;
        this.proxyServer = proxyServer1;
        this.serverNames = new ArrayList<>();
        loadConfig();
        logger.info("Mein aller erstes Plugin wurde geladen!");
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new ConnectionListener(proxyServer));
        CommandManager commandManager = proxyServer.getCommandManager();
        proxyServer.getCommandManager().register("fhas", new FreeHasCommand(proxyServer, serverNames));

        commandManager
                .register(commandManager.metaBuilder("kickall").build(),
                        new KickallCommand(proxyServer));
    }

    private void loadConfig() {
        try {
            Path configPath = Path.of("plugins/velocity-plugin/config.yml");
            if (!Files.exists(configPath)) {
                // Wenn die Datei nicht existiert, erzeuge eine Standardkonfiguration
                Files.createDirectories(configPath.getParent());
                try (InputStream defaultConfig = getClass().getResourceAsStream("/config.yml")) {
                    Files.copy(defaultConfig, configPath);
                }
            }

            // Lese die YAML Datei ein
            Yaml yaml = new Yaml(new Constructor(Config.class));
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                Config config = yaml.load(inputStream);
                this.serverNames = config.getServers();
                logger.info("Server in der Konfiguration: " + String.join(", ", serverNames));
            }
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Konfiguration", e);
        }
    }

    public static class Config {
        private List<String> servers;

        public List<String> getServers() {
            return servers;
        }

        public void setServers(List<String> servers) {
            this.servers = servers;
        }
    }
}
