package xyz.galaxyy.simplesellwand.hooks.shops;

import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.api.objects.SellPrice;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.EconomyType;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.galaxyy.simplesellwand.hooks.ShopHook;

public final class EconomyShopGUI implements ShopHook {
  @Override
  public Double getSellPrice(Player player, ItemStack itemStack) {
    Optional<SellPrice> sellPrice = EconomyShopGUIHook.getSellPrice(player, itemStack);
    if (sellPrice.isPresent()) {
      return sellPrice.get().getPrice(new EcoType(EconomyType.VAULT));
    }
    return 0.0;
  }
}
