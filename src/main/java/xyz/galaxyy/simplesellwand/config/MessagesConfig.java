package xyz.galaxyy.simplesellwand.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface MessagesConfig {
  @ConfKey("sold-items")
  @DefaultString("<green>Sold <item_count> items for <yellow>$<sell_price></yellow>!You have <yellow><uses_left></yellow> uses left on this wand.</green>")
  @ConfComments({ "The message sent to the player when they sell items", "Placeholders:",
      "<item_count> - The amount of items sold", "<sell_price> - The total sell price of the items sold",
      "<uses_left> - The amount of uses left on the wand" })
  String soldItems();

  @ConfKey("no-items-sold")
  @DefaultString("<red>No items were able to be sold.</red>")
  @ConfComments("The message sent to the player when they try to sell items but none of them are sellable.")
  String noItemsSold();

  @ConfKey("no-uses-left")
  @DefaultString("<red>You have no uses left on this wand.</red>")
  @ConfComments("The message sent to the player when they try to use a wand but they have no uses left.")
  String noUsesLeft();
}
