package xyz.galaxyy.simplesellwand.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.galaxyy.simplesellwand.PDCKeys;
import xyz.galaxyy.simplesellwand.SimpleSellWand;
import xyz.galaxyy.simplesellwand.config.WandConfig;

public final class SetWandCommand extends Command {
  public SetWandCommand() {
    super(
        "setwand",
        "Sets your currently held item as a sell wand.",
        "/setwand",
        List.of("givewand"));
    this.setPermission("simplesellwand.setwand");
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendRichMessage("<red>Only players can use this command.");
      return true;
    }
    if (args.length != 1) {
      sender.sendRichMessage("<red>Usage: /givewand <wandType>");
      return true;
    }
    Player player = (Player) sender;
    ItemStack itemStack = player.getInventory().getItemInMainHand();
    if (itemStack.getType() == Material.AIR) {
      player.sendRichMessage("<red>You must be holding an item to use this command.");
      return true;
    }
    ItemMeta itemMeta = itemStack.getItemMeta();
    PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
    String wandType = args[0];
    if (SimpleSellWand.getInstance().getConfigManager().getConfigData().wands().get(wandType)
        == null) {
      player.sendRichMessage("<red>That wand type does not exist.");
      return true;
    }
    WandConfig wand =
        SimpleSellWand.getInstance().getConfigManager().getConfigData().wands().get(wandType);
    pdc.set(PDCKeys.WandType, PersistentDataType.STRING, wandType);
    pdc.set(PDCKeys.IsWand, PersistentDataType.BOOLEAN, true);
    pdc.set(PDCKeys.UsageLeft, PersistentDataType.INTEGER, wand.usageLimit());
    itemStack.setItemMeta(itemMeta);
    player.sendRichMessage("<green>Successfully set your wand.");
    return true;
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
    if (args.length != 1) {
      return new ArrayList<>();
    }
    List<String> wandTypes = new ArrayList<>();
    wandTypes.addAll(
        SimpleSellWand.getInstance().getConfigManager().getConfigData().wands().keySet());
    return wandTypes;
  }
}
