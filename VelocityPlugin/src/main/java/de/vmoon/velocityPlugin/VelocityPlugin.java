package de.vmoon.velocityPlugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.vmoon.velocityPlugin.Listeners.ConnectionListener;
import de.vmoon.velocityPlugin.commands.KickallCommand;
import org.slf4j.Logger;

@Plugin
        (
        id = "velocityplugin",
        name = "VelocityPlugin",
        version = "1.0",
        description = "A Velocity Plugin by VoidableMoon",
        url = "velocity.vmoon.de",
        authors = {"VoidableMoon884"}
)

public class VelocityPlugin {

    private final Logger logger;
    private final ProxyServer proxyServer;

    @Inject
    public VelocityPlugin(Logger logger, ProxyServer proxyServer1) {
        this.logger = logger;
        this.proxyServer = proxyServer1;
        logger.info("Mein aller erstes Plugin wurde geladen!");
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new ConnectionListener(proxyServer));
        CommandManager commandManager = proxyServer.getCommandManager();

        commandManager
                .register(commandManager.metaBuilder("kickall").build(),
                        new KickallCommand(proxyServer));
    }
}
