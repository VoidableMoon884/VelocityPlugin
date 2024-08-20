package de.vmoon.velocityPlugin.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.vmoon.velocityPlugin.VelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadConfigsCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public ReloadConfigsCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        try {
            plugin.loadConfig(); // Lade die Konfiguration neu
            source.sendMessage(Component.text("Konfiguration erfolgreich neu geladen.", NamedTextColor.GREEN));
        } catch (Exception e) {
            source.sendMessage(Component.text("Fehler beim Neuladen der Konfiguration: " + e.getMessage(), NamedTextColor.RED));
            plugin.getLogger().error("Fehler beim Neuladen der Konfiguration", e);
        }
    }
}
