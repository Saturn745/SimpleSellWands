package xyz.galaxyy.simplesellwand.hooks;

import xyz.galaxyy.simplesellwand.SimpleSellWand;
import xyz.galaxyy.simplesellwand.hooks.economys.VaultEconomy;
import xyz.galaxyy.simplesellwand.hooks.shops.EconomyShopGUI;

public final class Hooks {
  private static EconomyHook economy = null;
  private static ShopHook shop = null;

  // We only need one instance of this so we can just use a static initializer
  public static void init() {
    hookEconomy();
    hookShop();
  }

  private static void hookEconomy() {
    String economyName = SimpleSellWand.getInstance().getConfigManager().getConfigData().economyPlugin();
    if (economyName == null || economyName.toLowerCase().equals("none")) {
      SimpleSellWand.getInstance().getLogger().severe("No economy plugin was specified in the config. Disabling...");
      SimpleSellWand.getInstance().getServer().getPluginManager().disablePlugin(SimpleSellWand.getInstance());
      return;
    }

    switch (economyName.toLowerCase()) {
      case "vault":
        economy = new VaultEconomy();
        break;
      default:
        SimpleSellWand.getInstance().getLogger().severe("Unknown economy plugin '" + economyName + "'. Disabling...");
        SimpleSellWand.getInstance().getServer().getPluginManager().disablePlugin(SimpleSellWand.getInstance());
        break;
    }
  }

  private static void hookShop() {
    String shopName = SimpleSellWand.getInstance().getConfigManager().getConfigData().shopPlugin();
    if (shopName == null || shopName.toLowerCase().equals("none")) {
      SimpleSellWand.getInstance().getLogger().severe("No shop plugin was specified in the config. Disabling...");
      SimpleSellWand.getInstance().getServer().getPluginManager().disablePlugin(SimpleSellWand.getInstance());
      return;
    }

    switch (shopName.toLowerCase()) {
      case "economyshopgui":
      case "economyshopgui-premium":
        shop = new EconomyShopGUI();
        break;
      default:
        SimpleSellWand.getInstance().getLogger().severe("Unknown shop plugin '" + shopName + "'. Disabling...");
        SimpleSellWand.getInstance().getServer().getPluginManager().disablePlugin(SimpleSellWand.getInstance());
        break;
    }
  }

  public static ShopHook getShop() {
    return shop;
  }

  public static EconomyHook getEconomy() {
    return economy;
  }
}
