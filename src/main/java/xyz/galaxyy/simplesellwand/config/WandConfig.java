package xyz.galaxyy.simplesellwand.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.ConfDefault.DefaultBoolean;
import space.arim.dazzleconf.annote.ConfDefault.DefaultInteger;

public interface WandConfig {
  @ConfKey("multiplier")
  @ConfComments("The multiplier of the wand. This is used to multiply the sell price of an item.")
  @DefaultInteger(0)
  int multiplier();

  @ConfKey("usage-limit")
  @ConfComments("The amount of times the wand can be used before it becomes unusable. Set to -1 for infinite uses.")
  @DefaultInteger(-1)
  int usageLimit();

  @ConfKey("remove-when-used-up")
  @ConfComments({
      "Whether or not the wand should be removed from the player's inventory when all of its uses are used up.",
      "This only applies if the usage limit is not -1." })
  @DefaultBoolean(true)
  boolean removeWhenUsedUp();
}
