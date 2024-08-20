package de.vmoon.velocityPlugin.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class ServerCommands implements SimpleCommand {

    private final ProxyServer proxyServer;
    private final String targetServerName;

    public ServerCommands(ProxyServer proxyServer, String targetServerName) {
        this.proxyServer = proxyServer;
        this.targetServerName = targetServerName;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Dieser Befehl kann nur von einem Spieler ausgeführt werden.", NamedTextColor.RED));
            return;
        }

        Player player = (Player) source;
        Optional<RegisteredServer> optionalServer = proxyServer.getServer(targetServerName);

        if (optionalServer.isPresent()) {
            RegisteredServer targetServer = optionalServer.get();
            player.createConnectionRequest(targetServer).connect().handle((result, throwable) -> {
                if (throwable != null || !result.isSuccessful()) {
                    player.sendMessage(Component.text("Verbindung zum Server " + targetServerName + " fehlgeschlagen.", NamedTextColor.RED));
                } else {
                    player.sendMessage(Component.text("Du wirst zum Server " + targetServerName + " verbunden.", NamedTextColor.GREEN));
                }
                return null;
            });
        } else {
            player.sendMessage(Component.text("Der Server " + targetServerName + " wurde nicht gefunden.", NamedTextColor.RED));
        }
    }
}
