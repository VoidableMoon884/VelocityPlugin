package de.vmoon.velocityPlugin.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FreeHasCommand implements SimpleCommand {

    private final ProxyServer server;
    private final List<String> serverNames;

    public FreeHasCommand(ProxyServer server, List<String> serverNames) {
        this.server = server;
        this.serverNames = serverNames;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Dieser Befehl kann nur von einem Spieler ausgeführt werden.", NamedTextColor.RED));
            return;
        }

        Player player = (Player) source;

        String currentServerName = player.getCurrentServer().map(serverConnection -> serverConnection.getServerInfo().getName()).orElse(null);
        List<String> availableServers = serverNames.stream().filter(name -> !name.equals(currentServerName)).collect(Collectors.toList());

        Collections.shuffle(availableServers);
        attemptConnection(player, availableServers);
    }

    private void attemptConnection(Player player, List<String> servers) {
        if (servers.isEmpty()) {
            player.sendMessage(Component.text("Es konnte kein freier Server gefunden werden.", NamedTextColor.RED));
            return;
        }

        String serverName = servers.get(0);
        Optional<RegisteredServer> optionalServer = server.getServer(serverName);

        if (optionalServer.isPresent()) {
            RegisteredServer targetServer = optionalServer.get();

            if (targetServer.getPlayersConnected().size() <= 1) {
                player.createConnectionRequest(targetServer).connect().handle((result, throwable) -> {
                    if (throwable != null || !result.isSuccessful()) {
                        attemptConnection(player, servers.subList(1, servers.size()));
                    } else {
                        player.sendMessage(Component.text("Du wurdest zu " + serverName + " verbunden.", NamedTextColor.GREEN));
                    }
                    return null;
                });
            } else {
                attemptConnection(player, servers.subList(1, servers.size()));
            }
        } else {
            attemptConnection(player, servers.subList(1, servers.size()));
        }
    }
}
