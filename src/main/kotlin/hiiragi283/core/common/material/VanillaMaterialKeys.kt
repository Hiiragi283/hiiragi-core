package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.ImmutableTable
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object VanillaMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey.of((HiiragiCoreAPI.id(path)))

    //    Fuels    //

    @JvmStatic
    val COAL: HTMaterialKey = create("coal")

    @JvmStatic
    val CHARCOAL: HTMaterialKey = create("charcoal")

    //    Minerals    //

    @JvmStatic
    val REDSTONE: HTMaterialKey = create("redstone")

    @JvmStatic
    val GLOWSTONE: HTMaterialKey = create("glowstone")

    //    Gems    //

    @JvmStatic
    val LAPIS: HTMaterialKey = create("lapis")

    @JvmStatic
    val QUARTZ: HTMaterialKey = create("quartz")

    @JvmStatic
    val AMETHYST: HTMaterialKey = create("amethyst")

    @JvmStatic
    val DIAMOND: HTMaterialKey = create("diamond")

    @JvmStatic
    val EMERALD: HTMaterialKey = create("emerald")

    @JvmStatic
    val ECHO: HTMaterialKey = create("echo")

    //    Pearls    //

    @JvmStatic
    val ENDER: HTMaterialKey = create("ender")

    //    Metals    //

    @JvmStatic
    val COPPER: HTMaterialKey = create("copper")

    @JvmStatic
    val IRON: HTMaterialKey = create("iron")

    @JvmStatic
    val GOLD: HTMaterialKey = create("gold")

    //    Alloys    //

    @JvmStatic
    val NETHERITE: HTMaterialKey = create("netherite")

    //    Others    //

    @JvmStatic
    val WOOD: HTMaterialKey = create("wood")

    @JvmStatic
    val GLASS: HTMaterialKey = create("glass")

    @JvmStatic
    val STONE: HTMaterialKey = create("stone")

    @JvmStatic
    val OBSIDIAN: HTMaterialKey = create("obsidian")

    //    Item    //

    @JvmStatic
    val INGREDIENTS: ImmutableTable<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>> = buildTable {
        fun add(prefix: HTTagPrefix, material: HTMaterialLike, item: Item) {
            this[prefix, material.asMaterialKey()] = item.toHolderLike()
        }

        // Fuel
        add(CommonTagPrefixes.BLOCK, COAL, Items.COAL_BLOCK)
        add(CommonTagPrefixes.FUEL, COAL, Items.COAL)

        add(CommonTagPrefixes.FUEL, CHARCOAL, Items.CHARCOAL)
        // Minerals
        add(CommonTagPrefixes.BLOCK, REDSTONE, Items.REDSTONE_BLOCK)
        add(CommonTagPrefixes.DUST, REDSTONE, Items.REDSTONE)

        add(CommonTagPrefixes.BLOCK, GLOWSTONE, Items.GLOWSTONE)
        add(CommonTagPrefixes.DUST, GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        add(CommonTagPrefixes.BLOCK, LAPIS, Items.LAPIS_BLOCK)
        add(CommonTagPrefixes.GEM, LAPIS, Items.LAPIS_LAZULI)

        add(CommonTagPrefixes.BLOCK, QUARTZ, Items.QUARTZ_BLOCK)
        add(CommonTagPrefixes.GEM, QUARTZ, Items.QUARTZ)

        add(CommonTagPrefixes.BLOCK, AMETHYST, Items.AMETHYST_BLOCK)
        add(CommonTagPrefixes.GEM, AMETHYST, Items.AMETHYST_SHARD)

        add(CommonTagPrefixes.BLOCK, DIAMOND, Items.DIAMOND_BLOCK)
        add(CommonTagPrefixes.GEM, DIAMOND, Items.DIAMOND)

        add(CommonTagPrefixes.BLOCK, EMERALD, Items.EMERALD_BLOCK)
        add(CommonTagPrefixes.GEM, EMERALD, Items.EMERALD)

        add(CommonTagPrefixes.GEM, ECHO, Items.ECHO_SHARD)
        // Pearl
        add(CommonTagPrefixes.PEARL, ENDER, Items.ENDER_PEARL)
        // Metals
        add(CommonTagPrefixes.ORE, COPPER, Items.COPPER_ORE)
        add(CommonTagPrefixes.RAW_BLOCK, COPPER, Items.RAW_COPPER_BLOCK)
        add(CommonTagPrefixes.BLOCK, COPPER, Items.COPPER_BLOCK)
        add(CommonTagPrefixes.RAW, COPPER, Items.RAW_COPPER)
        add(CommonTagPrefixes.INGOT, COPPER, Items.COPPER_INGOT)

        add(CommonTagPrefixes.ORE, IRON, Items.IRON_ORE)
        add(CommonTagPrefixes.RAW_BLOCK, IRON, Items.RAW_IRON_BLOCK)
        add(CommonTagPrefixes.BLOCK, IRON, Items.IRON_BLOCK)
        add(CommonTagPrefixes.RAW, IRON, Items.RAW_IRON)
        add(CommonTagPrefixes.INGOT, IRON, Items.IRON_INGOT)
        add(CommonTagPrefixes.NUGGET, IRON, Items.IRON_NUGGET)

        add(CommonTagPrefixes.ORE, GOLD, Items.GOLD_ORE)
        add(CommonTagPrefixes.RAW_BLOCK, GOLD, Items.RAW_GOLD_BLOCK)
        add(CommonTagPrefixes.BLOCK, GOLD, Items.GOLD_BLOCK)
        add(CommonTagPrefixes.RAW, GOLD, Items.RAW_GOLD)
        add(CommonTagPrefixes.INGOT, GOLD, Items.GOLD_INGOT)
        add(CommonTagPrefixes.NUGGET, GOLD, Items.GOLD_NUGGET)
        // Alloys
        add(CommonTagPrefixes.BLOCK, NETHERITE, Items.NETHERITE_BLOCK)
        add(CommonTagPrefixes.SCRAP, NETHERITE, Items.NETHERITE_SCRAP)
        add(CommonTagPrefixes.INGOT, NETHERITE, Items.NETHERITE_INGOT)
    }

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
