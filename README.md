# SimpleSellWands
<a href=https://modrinth.com/plugin/simplesellwands><img alt="modrinth" height="54" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg"></a>

A simple PaperMC plugin which provides sell wands for your players to quickly sell chests of items!


# Comamnds & Permissions
**Command: `/setwand`**
- **Description**: Sets your currently held item as a sell wand.
- **Aliases**: `/givewand`
- **Usage**: `/setwand WAND`
- **Permission**: `simplesellwand.setwand`

**Command: `/sellwandreload`**
- **Description**: Reloads the config
- **Aliases**: `/sellwandrl`
- **Usage**: `/sellwandreload`
- **Permission**: `simplesellwand.reload`

# Config
<details>
  <summary><b>Default Configuration (HOCON)</b></summary>

```hocon
# The economy plugin to use. Currently only Vault is supported.
economy-plugin=Vault
messages {
    # The message sent to the player when they try to sell items but none of them are sellable.
    no-items-sold="<red>No items were able to be sold.</red>"
    # The message sent to the player when they sell items. <item_count> is replaced with the amount of items sold, and <sell_price> is replaced with the amount of money they received.
    sold-items="<green>Sold <item_count> items for <yellow>$<sell_price></yellow>!</green>"
}
# The shop plugin to use. Currently only EconomyShopGUI is supported.
shop-plugin=EconomyShopGUI
wands {
    default {
        # The multiplier of the wand. This is used to multiply the sell price of an item.
        multiplier=0
    }
}
```
</details>

## Wand Config Options

| Key         | Type | Default Value | Description                                        |
|-------------|------|---------------|----------------------------------------------------|
| multiplier  | int  | 0             | The sell multiplier to apply when using this wand. |
| usage-limit | int  | 0             | **NOT IMPLEMENTED YET**                            |


# Supported Economy Plugins
- [x] Vault
- [ ] Treasury

# Supported Shop Plugins
- [x] EconomyShopGUI
- [x] EconomyShopGUI-Premium
- [ ] EssentialsX

If you would like to request a economy or shop plugin please open an issue.
