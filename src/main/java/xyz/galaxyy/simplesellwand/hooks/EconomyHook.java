package xyz.galaxyy.simplesellwand.hooks;

import org.bukkit.entity.Player;

public interface EconomyHook {
  void deposit(Player player, Double amount);
}
