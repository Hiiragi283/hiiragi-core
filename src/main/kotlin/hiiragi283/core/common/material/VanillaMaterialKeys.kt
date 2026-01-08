package hiiragi283.core.common.material

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object VanillaMaterialKeys {
    //    Fuels    //

    @JvmStatic
    val COAL: HTMaterialKey = HTMaterialKey.of("coal")

    @JvmStatic
    val CHARCOAL: HTMaterialKey = HTMaterialKey.of("charcoal")

    //    Minerals    //

    @JvmStatic
    val REDSTONE: HTMaterialKey = HTMaterialKey.of("redstone")

    @JvmStatic
    val GLOWSTONE: HTMaterialKey = HTMaterialKey.of("glowstone")

    //    Gems    //

    @JvmStatic
    val LAPIS: HTMaterialKey = HTMaterialKey.of("lapis")

    @JvmStatic
    val QUARTZ: HTMaterialKey = HTMaterialKey.of("quartz")

    @JvmStatic
    val AMETHYST: HTMaterialKey = HTMaterialKey.of("amethyst")

    @JvmStatic
    val DIAMOND: HTMaterialKey = HTMaterialKey.of("diamond")

    @JvmStatic
    val EMERALD: HTMaterialKey = HTMaterialKey.of("emerald")

    @JvmStatic
    val ECHO: HTMaterialKey = HTMaterialKey.of("echo")

    //    Pearls    //

    @JvmStatic
    val ENDER: HTMaterialKey = HTMaterialKey.of("ender")

    //    Metals    //

    @JvmStatic
    val COPPER: HTMaterialKey = HTMaterialKey.of("copper")

    @JvmStatic
    val IRON: HTMaterialKey = HTMaterialKey.of("iron")

    @JvmStatic
    val GOLD: HTMaterialKey = HTMaterialKey.of("gold")

    //    Alloys    //

    @JvmStatic
    val NETHERITE: HTMaterialKey = HTMaterialKey.of("netherite")

    //    Others    //

    @JvmStatic
    val WOOD: HTMaterialKey = HTMaterialKey.of("wood")

    @JvmStatic
    val STONE: HTMaterialKey = HTMaterialKey.of("stone")

    @JvmStatic
    val OBSIDIAN: HTMaterialKey = HTMaterialKey.of("obsidian")

    //    Item    //

    @JvmStatic
    val INGREDIENTS: HTMaterialTable<HTMaterialPrefix, HTItemHolderLike<Item>> = buildTable {
        fun add(prefix: HTPrefixLike, material: HTMaterialLike, item: Item) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = item.toHolderLike()
        }

        // Fuel
        add(HCMaterialPrefixes.STORAGE_BLOCK, COAL, Items.COAL_BLOCK)
        add(HCMaterialPrefixes.FUEL, COAL, Items.COAL)

        add(HCMaterialPrefixes.FUEL, CHARCOAL, Items.CHARCOAL)
        // Minerals
        add(HCMaterialPrefixes.STORAGE_BLOCK, REDSTONE, Items.REDSTONE_BLOCK)
        add(HCMaterialPrefixes.DUST, REDSTONE, Items.REDSTONE)

        add(HCMaterialPrefixes.STORAGE_BLOCK, GLOWSTONE, Items.GLOWSTONE)
        add(HCMaterialPrefixes.DUST, GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        add(HCMaterialPrefixes.STORAGE_BLOCK, LAPIS, Items.LAPIS_BLOCK)
        add(HCMaterialPrefixes.GEM, LAPIS, Items.LAPIS_LAZULI)

        add(HCMaterialPrefixes.STORAGE_BLOCK, QUARTZ, Items.QUARTZ_BLOCK)
        add(HCMaterialPrefixes.GEM, QUARTZ, Items.QUARTZ)

        add(HCMaterialPrefixes.STORAGE_BLOCK, AMETHYST, Items.AMETHYST_BLOCK)
        add(HCMaterialPrefixes.GEM, AMETHYST, Items.AMETHYST_SHARD)

        add(HCMaterialPrefixes.STORAGE_BLOCK, DIAMOND, Items.DIAMOND_BLOCK)
        add(HCMaterialPrefixes.GEM, DIAMOND, Items.DIAMOND)

        add(HCMaterialPrefixes.STORAGE_BLOCK, EMERALD, Items.EMERALD_BLOCK)
        add(HCMaterialPrefixes.GEM, EMERALD, Items.EMERALD)

        add(HCMaterialPrefixes.GEM, ECHO, Items.ECHO_SHARD)
        // Pearl
        add(HCMaterialPrefixes.PEARL, ENDER, Items.ENDER_PEARL)
        // Metals
        add(HCMaterialPrefixes.ORE, COPPER, Items.COPPER_ORE)
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW, COPPER, Items.RAW_COPPER_BLOCK)
        add(HCMaterialPrefixes.STORAGE_BLOCK, COPPER, Items.COPPER_BLOCK)
        add(HCMaterialPrefixes.RAW_MATERIAL, COPPER, Items.RAW_COPPER)
        add(HCMaterialPrefixes.INGOT, COPPER, Items.COPPER_INGOT)

        add(HCMaterialPrefixes.ORE, IRON, Items.IRON_ORE)
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW, IRON, Items.RAW_IRON_BLOCK)
        add(HCMaterialPrefixes.STORAGE_BLOCK, IRON, Items.IRON_BLOCK)
        add(HCMaterialPrefixes.RAW_MATERIAL, IRON, Items.RAW_IRON)
        add(HCMaterialPrefixes.INGOT, IRON, Items.IRON_INGOT)
        add(HCMaterialPrefixes.NUGGET, IRON, Items.IRON_NUGGET)

        add(HCMaterialPrefixes.ORE, GOLD, Items.GOLD_ORE)
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW, GOLD, Items.RAW_GOLD_BLOCK)
        add(HCMaterialPrefixes.STORAGE_BLOCK, GOLD, Items.GOLD_BLOCK)
        add(HCMaterialPrefixes.RAW_MATERIAL, GOLD, Items.RAW_GOLD)
        add(HCMaterialPrefixes.INGOT, GOLD, Items.GOLD_INGOT)
        add(HCMaterialPrefixes.NUGGET, GOLD, Items.GOLD_NUGGET)
        // Alloys
        add(HCMaterialPrefixes.STORAGE_BLOCK, NETHERITE, Items.NETHERITE_BLOCK)
        add(HCMaterialPrefixes.SCRAP, NETHERITE, Items.NETHERITE_SCRAP)
        add(HCMaterialPrefixes.INGOT, NETHERITE, Items.NETHERITE_INGOT)
    }.let(::HTMaterialTable)

    /*val ARMOR_TABLE: ImmutableTable<HTArmorVariant, HTMaterialKey, Item> = buildTable {
        // Iron
        this[HTArmorVariant.HELMET, IRON] = Items.IRON_HELMET
        this[HTArmorVariant.CHESTPLATE, IRON] = Items.IRON_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, IRON] = Items.IRON_LEGGINGS
        this[HTArmorVariant.BOOTS, IRON] = Items.IRON_BOOTS
        // Gold
        this[HTArmorVariant.HELMET, GOLD] = Items.GOLDEN_HELMET
        this[HTArmorVariant.CHESTPLATE, GOLD] = Items.GOLDEN_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, GOLD] = Items.GOLDEN_LEGGINGS
        this[HTArmorVariant.BOOTS, GOLD] = Items.GOLDEN_BOOTS
        // Diamond
        this[HTArmorVariant.HELMET, DIAMOND] = Items.DIAMOND_HELMET
        this[HTArmorVariant.CHESTPLATE, DIAMOND] = Items.DIAMOND_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, DIAMOND] = Items.DIAMOND_LEGGINGS
        this[HTArmorVariant.BOOTS, DIAMOND] = Items.DIAMOND_BOOTS
        // Netherite
        this[HTArmorVariant.HELMET, NETHERITE] = Items.NETHERITE_HELMET
        this[HTArmorVariant.CHESTPLATE, NETHERITE] = Items.NETHERITE_CHESTPLATE
        this[HTArmorVariant.LEGGINGS, NETHERITE] = Items.NETHERITE_LEGGINGS
        this[HTArmorVariant.BOOTS, NETHERITE] = Items.NETHERITE_BOOTS
    }

    @JvmStatic
    val TOOL_TABLE: ImmutableTable<HTToolVariant, HTMaterialKey, Item> = buildTable {
        // Wooden
        this[VanillaToolVariant.SHOVEL, WOOD] = Items.WOODEN_SHOVEL
        this[VanillaToolVariant.PICKAXE, WOOD] = Items.WOODEN_PICKAXE
        this[VanillaToolVariant.AXE, WOOD] = Items.WOODEN_AXE
        this[VanillaToolVariant.HOE, WOOD] = Items.WOODEN_HOE
        this[VanillaToolVariant.SWORD, WOOD] = Items.WOODEN_SWORD
        // Stone
        // Iron
        this[VanillaToolVariant.SHOVEL, IRON] = Items.IRON_SHOVEL
        this[VanillaToolVariant.PICKAXE, IRON] = Items.IRON_PICKAXE
        this[VanillaToolVariant.AXE, IRON] = Items.IRON_AXE
        this[VanillaToolVariant.HOE, IRON] = Items.IRON_HOE
        this[VanillaToolVariant.SWORD, IRON] = Items.IRON_SWORD

        this[VanillaToolVariant.SHEARS, IRON] = Items.SHEARS
        // Golden
        this[VanillaToolVariant.SHOVEL, GOLD] = Items.GOLDEN_SHOVEL
        this[VanillaToolVariant.PICKAXE, GOLD] = Items.GOLDEN_PICKAXE
        this[VanillaToolVariant.AXE, GOLD] = Items.GOLDEN_AXE
        this[VanillaToolVariant.HOE, GOLD] = Items.GOLDEN_HOE
        this[VanillaToolVariant.SWORD, GOLD] = Items.GOLDEN_SWORD
        // Diamond
        this[VanillaToolVariant.SHOVEL, DIAMOND] = Items.DIAMOND_SHOVEL
        this[VanillaToolVariant.PICKAXE, DIAMOND] = Items.DIAMOND_PICKAXE
        this[VanillaToolVariant.AXE, DIAMOND] = Items.DIAMOND_AXE
        this[VanillaToolVariant.HOE, DIAMOND] = Items.DIAMOND_HOE
        this[VanillaToolVariant.SWORD, DIAMOND] = Items.DIAMOND_SWORD
        // Netherite
        this[VanillaToolVariant.SHOVEL, NETHERITE] = Items.NETHERITE_SHOVEL
        this[VanillaToolVariant.PICKAXE, NETHERITE] = Items.NETHERITE_PICKAXE
        this[VanillaToolVariant.AXE, NETHERITE] = Items.NETHERITE_AXE
        this[VanillaToolVariant.HOE, NETHERITE] = Items.NETHERITE_HOE
        this[VanillaToolVariant.SWORD, NETHERITE] = Items.NETHERITE_SWORD
    }*/
}
