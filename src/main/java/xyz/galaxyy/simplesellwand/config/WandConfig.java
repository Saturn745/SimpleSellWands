package xyz.galaxyy.simplesellwand.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.ConfDefault.DefaultInteger;

public interface WandConfig {
  @ConfKey("multiplier")
  @ConfComments("The multiplier of the wand. This is used to multiply the sell price of an item.")
  @DefaultInteger(0)
  int multiplier();

}
