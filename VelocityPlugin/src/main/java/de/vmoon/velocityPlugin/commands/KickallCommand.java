package de.vmoon.velocityPlugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.stream.Collectors;

public class KickallCommand implements SimpleCommand {

    private final ProxyServer proxyServer;

    public KickallCommand(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length != 1) {
            proxyServer.getAllPlayers()
                    .forEach(player -> player.disconnect
                            (Component.text("Du wurdest gekickt!", NamedTextColor.RED)));
            return;
        }

        Optional<RegisteredServer> server = proxyServer.getServer(invocation.arguments()[0]);

        if (server.isEmpty()) {
            invocation.source().sendMessage(Component.text("Der Server wurde nicht gefunden!", NamedTextColor.RED));
            return;
        }

        server.get().getPlayersConnected()
                .forEach(player -> player.disconnect
                        (Component.text("Du wurdest gekickt!", NamedTextColor.RED)));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 1) {
            return proxyServer.getAllServers()
                    .stream()
                    .map(registeredServer -> registeredServer.getServerInfo().getName())
                    .filter(s -> s.startsWith(invocation.arguments()[0]))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
