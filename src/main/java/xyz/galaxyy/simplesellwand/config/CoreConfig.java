package xyz.galaxyy.simplesellwand.config;

import java.util.Map;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;
import space.arim.dazzleconf.annote.ConfDefault.DefaultObject;
import space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface CoreConfig {
  @ConfKey("economy-plugin")
  @DefaultString("Vault")
  @ConfComments("The economy plugin to use. Currently only Vault is supported.")
  String economyPlugin();

  @ConfKey("shop-plugin")
  @DefaultString("none")
  @ConfComments("The shop plugin to use. Currently only EconomyShopGUI is supported.")
  String shopPlugin();

  static Map<String, WandConfig> defaultWands() {
    return Map.of(
        "default", new WandConfig() {
          @Override
          public int multiplier() {
            return 0;
          }

          @Override
          public int usageLimit() {
            return -1;
          }

          @Override
          public boolean removeWhenUsedUp() {
            return true;
          }
        });
  }

  @ConfKey("wands")
  @DefaultObject("defaultWands")
  Map<String, @SubSection WandConfig> wands();

  @ConfKey("messages")
  @SubSection
  MessagesConfig messages();

}
