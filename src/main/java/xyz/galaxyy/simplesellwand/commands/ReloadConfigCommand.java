package xyz.galaxyy.simplesellwand.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.galaxyy.simplesellwand.SimpleSellWand;

public final class ReloadConfigCommand extends Command {
  public ReloadConfigCommand() {
    super("sellwandreload", "Reloads the config", "/sellwandreload", List.of("sellwandrl"));
    this.setPermission("simplesellwand.reload");
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    SimpleSellWand.getInstance().getConfigManager().reloadConfig();
    sender.sendRichMessage("<green>Config reloaded.");
    return true;
  }
}
