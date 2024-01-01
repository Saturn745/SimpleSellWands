package xyz.galaxyy.simplesellwand.listeners;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import xyz.galaxyy.simplesellwand.PDCKeys;
import xyz.galaxyy.simplesellwand.SimpleSellWand;
import xyz.galaxyy.simplesellwand.config.WandConfig;
import xyz.galaxyy.simplesellwand.hooks.Hooks;

public final class PlayerInteractListener implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK
        || !(event.getClickedBlock().getState() instanceof Chest)) {
      return;
    }
    if (event.getItem() == null) {
      return;
    }
    if (!event.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(PDCKeys.IsWand,
        PersistentDataType.BOOLEAN, false)) {
      return;
    }
    WandConfig wand = SimpleSellWand.getInstance().getConfigManager().getConfigData().wands()
        .get(event.getItem().getItemMeta().getPersistentDataContainer().getOrDefault(PDCKeys.WandType,
            PersistentDataType.STRING, "default"));
    if (wand == null) {
      return; // Not sure how this would happen, but just in case.
    }

    Chest chest = (Chest) event.getClickedBlock().getState();
    Double totalSellPrice = 0.0;
    int itemsSold = 0;
    for (int i = 0; i < chest.getInventory().getSize(); i++) {
      if (chest.getInventory().getItem(i) == null) {
        // No item in this slot
        continue; // Skip to the next item
      }
      ItemStack itemStack = chest.getInventory().getItem(i);
      Double sellPrice = Hooks.getShop().getSellPrice(event.getPlayer(), itemStack);
      if (sellPrice == 0.0) {
        // No sell price, or not a sellable item, so we just ignore this item
        continue; // Skip to the next item
      }
      chest.getInventory().setItem(i, null);
      totalSellPrice += sellPrice;
      itemsSold++;
    }
    if (totalSellPrice == 0.0) {
      // No items were sold
      event.getPlayer().sendMessage(MiniMessage.miniMessage()
          .deserialize(SimpleSellWand.getInstance().getConfigManager().getConfigData().messages().noItemsSold()));
      return;
    }
    if (wand.multiplier() != 0) {
      totalSellPrice *= wand.multiplier();
    }
    Hooks.getEconomy().deposit(event.getPlayer(), totalSellPrice);
    event.getPlayer()
        .sendMessage(MiniMessage.miniMessage().deserialize(
            SimpleSellWand.getInstance().getConfigManager().getConfigData().messages().soldItems(),
            Placeholder.unparsed("item_count", String.valueOf(itemsSold)),
            Placeholder.unparsed("sell_price", String.valueOf(totalSellPrice)))); // TODO:
                                                                                  // Cache
                                                                                  // these
                                                                                  // messages?
    event.setCancelled(true);
  }
}
