package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

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

    @JvmStatic
    val PRISMARINE: HTMaterialKey = create("prismarine")

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

    //    Crops    //

    @JvmStatic
    val WHEAT: HTMaterialKey = create("wheat")

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

    @JvmField
    val BLOCKS: HTTable<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*, *>> = buildTable {
        fun add(prefix: HTTagPrefix, material: HTMaterialLike, block: Block) {
            this[prefix, material.asMaterialKey()] = HTBlockHolderLike.Simple(block)
        }

        // Fuel
        add(CommonTagPrefixes.BLOCK, COAL, Blocks.COAL_BLOCK)
        // Mineral
        add(CommonTagPrefixes.BLOCK, REDSTONE, Blocks.REDSTONE_BLOCK)
        add(CommonTagPrefixes.BLOCK, GLOWSTONE, Blocks.GLOWSTONE)
        // Gem
        add(CommonTagPrefixes.BLOCK, LAPIS, Blocks.LAPIS_BLOCK)
        add(CommonTagPrefixes.BLOCK, QUARTZ, Blocks.QUARTZ_BLOCK)
        add(CommonTagPrefixes.BLOCK, AMETHYST, Blocks.AMETHYST_BLOCK)
        add(CommonTagPrefixes.BLOCK, DIAMOND, Blocks.DIAMOND_BLOCK)
        add(CommonTagPrefixes.BLOCK, EMERALD, Blocks.EMERALD_BLOCK)
        // Metal
        add(CommonTagPrefixes.ORE, COPPER, Blocks.COPPER_ORE)
        add(CommonTagPrefixes.RAW_BLOCK, COPPER, Blocks.RAW_COPPER_BLOCK)
        add(CommonTagPrefixes.BLOCK, COPPER, Blocks.COPPER_BLOCK)

        add(CommonTagPrefixes.ORE, IRON, Blocks.IRON_ORE)
        add(CommonTagPrefixes.RAW_BLOCK, IRON, Blocks.RAW_IRON_BLOCK)
        add(CommonTagPrefixes.BLOCK, IRON, Blocks.IRON_BLOCK)

        add(CommonTagPrefixes.ORE, GOLD, Blocks.GOLD_ORE)
        add(CommonTagPrefixes.RAW_BLOCK, GOLD, Blocks.RAW_GOLD_BLOCK)
        add(CommonTagPrefixes.BLOCK, GOLD, Blocks.GOLD_BLOCK)
        // Alloy
        add(CommonTagPrefixes.BLOCK, NETHERITE, Blocks.NETHERITE_BLOCK)
        // Crop
        add(CommonTagPrefixes.BLOCK, WHEAT, Blocks.HAY_BLOCK)
    }

    @JvmField
    val ITEMS: HTTable<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>> = buildTable {
        fun add(prefix: HTTagPrefix, material: HTMaterialLike, item: Item) {
            this[prefix, material.asMaterialKey()] = HTItemHolderLike.Simple(item)
        }

        // Fuel
        add(CommonTagPrefixes.FUEL, COAL, Items.COAL)
        add(CommonTagPrefixes.FUEL, CHARCOAL, Items.CHARCOAL)
        // Mineral
        add(CommonTagPrefixes.DUST, REDSTONE, Items.REDSTONE)
        add(CommonTagPrefixes.DUST, GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        add(CommonTagPrefixes.GEM, LAPIS, Items.LAPIS_LAZULI)
        add(CommonTagPrefixes.GEM, QUARTZ, Items.QUARTZ)
        add(CommonTagPrefixes.GEM, AMETHYST, Items.AMETHYST_SHARD)
        add(CommonTagPrefixes.GEM, DIAMOND, Items.DIAMOND)
        add(CommonTagPrefixes.GEM, EMERALD, Items.EMERALD)
        add(CommonTagPrefixes.GEM, ECHO, Items.ECHO_SHARD)
        add(CommonTagPrefixes.GEM, PRISMARINE, Items.PRISMARINE_CRYSTALS)
        // Pearl
        add(CommonTagPrefixes.PEARL, ENDER, Items.ENDER_PEARL)
        // Metal
        add(CommonTagPrefixes.RAW, COPPER, Items.RAW_COPPER)
        add(CommonTagPrefixes.INGOT, COPPER, Items.COPPER_INGOT)

        add(CommonTagPrefixes.RAW, IRON, Items.RAW_IRON)
        add(CommonTagPrefixes.INGOT, IRON, Items.IRON_INGOT)
        add(CommonTagPrefixes.NUGGET, IRON, Items.IRON_NUGGET)

        add(CommonTagPrefixes.RAW, GOLD, Items.RAW_GOLD)
        add(CommonTagPrefixes.INGOT, GOLD, Items.GOLD_INGOT)
        add(CommonTagPrefixes.NUGGET, GOLD, Items.GOLD_NUGGET)
        // Alloy
        add(CommonTagPrefixes.SCRAP, NETHERITE, Items.NETHERITE_SCRAP)
        add(CommonTagPrefixes.INGOT, NETHERITE, Items.NETHERITE_INGOT)
        // Crop
        add(CommonTagPrefixes.CROP, WHEAT, Items.WHEAT)
        add(CommonTagPrefixes.SEED, WHEAT, Items.WHEAT_SEEDS)
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
    }*/

    @JvmField
    val TOOLS: HTTable<HTToolType, HTMaterialKey, HTItemHolderLike<*>> = buildTable {
        fun add(toolType: HTToolType, material: HTMaterialLike, item: Item) {
            this[toolType, material.asMaterialKey()] = HTItemHolderLike.Simple(item)
        }

        // Wooden
        add(CommonToolTypes.SHOVEL, WOOD, Items.WOODEN_SHOVEL)
        add(CommonToolTypes.PICKAXE, WOOD, Items.WOODEN_PICKAXE)
        add(CommonToolTypes.AXE, WOOD, Items.WOODEN_AXE)
        add(CommonToolTypes.HOE, WOOD, Items.WOODEN_HOE)
        add(CommonToolTypes.SWORD, WOOD, Items.WOODEN_SWORD)
        // Stone
        // Iron
        add(CommonToolTypes.SHOVEL, IRON, Items.IRON_SHOVEL)
        add(CommonToolTypes.PICKAXE, IRON, Items.IRON_PICKAXE)
        add(CommonToolTypes.AXE, IRON, Items.IRON_AXE)
        add(CommonToolTypes.HOE, IRON, Items.IRON_HOE)
        add(CommonToolTypes.SWORD, IRON, Items.IRON_SWORD)

        // add(CommonToolTypes.SHEARS, IRON, Items.SHEARS)
        // Golden
        add(CommonToolTypes.SHOVEL, GOLD, Items.GOLDEN_SHOVEL)
        add(CommonToolTypes.PICKAXE, GOLD, Items.GOLDEN_PICKAXE)
        add(CommonToolTypes.AXE, GOLD, Items.GOLDEN_AXE)
        add(CommonToolTypes.HOE, GOLD, Items.GOLDEN_HOE)
        add(CommonToolTypes.SWORD, GOLD, Items.GOLDEN_SWORD)
        // Diamond
        add(CommonToolTypes.SHOVEL, DIAMOND, Items.DIAMOND_SHOVEL)
        add(CommonToolTypes.PICKAXE, DIAMOND, Items.DIAMOND_PICKAXE)
        add(CommonToolTypes.AXE, DIAMOND, Items.DIAMOND_AXE)
        add(CommonToolTypes.HOE, DIAMOND, Items.DIAMOND_HOE)
        add(CommonToolTypes.SWORD, DIAMOND, Items.DIAMOND_SWORD)
        // Netherite
        add(CommonToolTypes.SHOVEL, NETHERITE, Items.NETHERITE_SHOVEL)
        add(CommonToolTypes.PICKAXE, NETHERITE, Items.NETHERITE_PICKAXE)
        add(CommonToolTypes.AXE, NETHERITE, Items.NETHERITE_AXE)
        add(CommonToolTypes.HOE, NETHERITE, Items.NETHERITE_HOE)
        add(CommonToolTypes.SWORD, NETHERITE, Items.NETHERITE_SWORD)
    }
}
