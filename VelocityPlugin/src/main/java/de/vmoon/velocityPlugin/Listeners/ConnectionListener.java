package de.vmoon.velocityPlugin.Listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ConnectionListener {

    private final ProxyServer proxyServer;

    public ConnectionListener(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        proxyServer.sendMessage(Component.text(event.getPlayer().getUsername(), NamedTextColor.RED)
                .append(Component.text(" hat den Server betreten.", NamedTextColor.GREEN)));
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        proxyServer.sendMessage(Component.text(event.getPlayer().getUsername(), NamedTextColor.RED)
                .append(Component.text(" hat den Server verlassen.", NamedTextColor.GREEN)));
    }
}
