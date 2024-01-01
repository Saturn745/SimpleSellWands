package xyz.galaxyy.simplesellwand.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface MessagesConfig {
  @ConfKey("sold-items")
  @DefaultString("<green>Sold <item_count> items for <yellow>$<sell_price></yellow>!</green>")
  @ConfComments("The message sent to the player when they sell items. <item_count> is replaced with the amount of items sold, and <sell_price> is replaced with the amount of money they received.")
  String soldItems();

  @ConfKey("no-items-sold")
  @DefaultString("<red>No items were able to be sold.</red>")
  @ConfComments("The message sent to the player when they try to sell items but none of them are sellable.")
  String noItemsSold();
}
