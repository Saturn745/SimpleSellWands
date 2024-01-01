package xyz.galaxyy.simplesellwand.hooks.economys;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import xyz.galaxyy.simplesellwand.SimpleSellWand;
import xyz.galaxyy.simplesellwand.hooks.EconomyHook;

public final class VaultEconomy implements EconomyHook {
  private Economy economy;

  public VaultEconomy() {
    if (SimpleSellWand.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
      SimpleSellWand.getInstance().getLogger().severe("Vault is not enabled. Disabling...");
      SimpleSellWand.getInstance().getServer().getPluginManager().disablePlugin(SimpleSellWand.getInstance());
      return;
    }

    RegisteredServiceProvider<Economy> rsp = SimpleSellWand.getInstance().getServer().getServicesManager()
        .getRegistration(Economy.class);
    if (rsp == null) {
      SimpleSellWand.getInstance().getLogger().severe("No economy plugin was found. Disabling...");
      SimpleSellWand.getInstance().getServer().getPluginManager().disablePlugin(SimpleSellWand.getInstance());
      return;
    }

    economy = rsp.getProvider();
    SimpleSellWand.getInstance().getLogger().info("Hooked into Vault.");
  }

  @Override
  public void deposit(Player player, Double amount) {
    economy.depositPlayer(player, amount);
  }
}
