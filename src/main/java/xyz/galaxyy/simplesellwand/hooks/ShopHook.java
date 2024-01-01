package xyz.galaxyy.simplesellwand.hooks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ShopHook {
  Double getSellPrice(Player player, ItemStack itemStack);
}
