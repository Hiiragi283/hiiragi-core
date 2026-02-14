package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterExistingPartEvent
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HTExistingPartHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerBlocks(event: HTRegisterExistingPartEvent.BlockEvent) {
        // Fuels
        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.COAL, Blocks.COAL_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.COAL, Blocks.DEEPSLATE_COAL_ORE)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL, Blocks.COAL_BLOCK)
        // Mineral
        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_BLOCK)

        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.GLOWSTONE, Blocks.GLOWSTONE)
        // Gem
        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.LAPIS, Blocks.DEEPSLATE_LAPIS_ORE)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_BLOCK)

        event.registerBlock(CommonTagPrefixes.ORE_NETHER, VanillaMaterialKeys.QUARTZ, Blocks.NETHER_QUARTZ_ORE)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, Blocks.QUARTZ_BLOCK)

        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.AMETHYST, Blocks.AMETHYST_BLOCK)

        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_BLOCK)

        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.EMERALD, Blocks.DEEPSLATE_EMERALD_ORE)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_BLOCK)
        // Metal
        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.COPPER, Blocks.COPPER_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.COPPER, Blocks.DEEPSLATE_COPPER_ORE)
        event.registerBlock(CommonTagPrefixes.RAW_BLOCK, VanillaMaterialKeys.COPPER, Blocks.RAW_COPPER_BLOCK)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COPPER, Blocks.COPPER_BLOCK)

        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.IRON, Blocks.IRON_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.IRON, Blocks.DEEPSLATE_IRON_ORE)
        event.registerBlock(CommonTagPrefixes.RAW_BLOCK, VanillaMaterialKeys.IRON, Blocks.RAW_IRON_BLOCK)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.IRON, Blocks.IRON_BLOCK)

        event.registerBlock(CommonTagPrefixes.ORE, VanillaMaterialKeys.GOLD, Blocks.GOLD_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.GOLD, Blocks.DEEPSLATE_GOLD_ORE)
        event.registerBlock(CommonTagPrefixes.ORE_NETHER, VanillaMaterialKeys.GOLD, Blocks.NETHER_GOLD_ORE)
        event.registerBlock(CommonTagPrefixes.RAW_BLOCK, VanillaMaterialKeys.GOLD, Blocks.RAW_GOLD_BLOCK)
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.GOLD, Blocks.GOLD_BLOCK)
        // Alloy
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.NETHERITE, Blocks.NETHERITE_BLOCK)
        // Crop
        event.registerBlock(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.WHEAT, Blocks.HAY_BLOCK)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerItems(event: HTRegisterExistingPartEvent.ItemEvent) {
        // Fuel
        event.registerItem(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, Items.COAL)
        event.registerItem(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, Items.CHARCOAL)
        // Mineral
        event.registerItem(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, Items.REDSTONE)
        event.registerItem(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ, Items.QUARTZ)
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND, Items.DIAMOND)
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD, Items.EMERALD)
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO, Items.ECHO_SHARD)
        event.registerItem(CommonTagPrefixes.DUST, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_SHARD)
        event.registerItem(CommonTagPrefixes.GEM, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_CRYSTALS)
        // Pearl
        event.registerItem(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER, Items.ENDER_PEARL)
        // Metal
        event.registerItem(CommonTagPrefixes.RAW, VanillaMaterialKeys.COPPER, Items.RAW_COPPER)
        event.registerItem(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER, Items.COPPER_INGOT)

        event.registerItem(CommonTagPrefixes.RAW, VanillaMaterialKeys.IRON, Items.RAW_IRON)
        event.registerItem(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, Items.IRON_INGOT)
        event.registerItem(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON, Items.IRON_NUGGET)

        event.registerItem(CommonTagPrefixes.RAW, VanillaMaterialKeys.GOLD, Items.RAW_GOLD)
        event.registerItem(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD, Items.GOLD_INGOT)
        event.registerItem(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.GOLD, Items.GOLD_NUGGET)
        // Alloy
        event.registerItem(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SCRAP)
        event.registerItem(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)
        // Crop
        event.registerItem(CommonTagPrefixes.CROP, VanillaMaterialKeys.WHEAT, Items.WHEAT)
        event.registerItem(CommonTagPrefixes.SEED, VanillaMaterialKeys.WHEAT, Items.WHEAT_SEEDS)
        // Other
        event.registerItem(CommonTagPrefixes.DUST, VanillaMaterialKeys.BLAZE, Items.BLAZE_POWDER)
        event.registerItem(CommonTagPrefixes.ROD, VanillaMaterialKeys.BLAZE, Items.BLAZE_ROD)

        event.registerItem(CommonTagPrefixes.DUST, VanillaMaterialKeys.BREEZE, Items.WIND_CHARGE)
        event.registerItem(CommonTagPrefixes.ROD, VanillaMaterialKeys.BREEZE, Items.BREEZE_ROD)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun registerTools(event: HTRegisterExistingPartEvent.ToolEvent) {
        // Wooden
        event.registerTool(CommonToolTypes.SHOVEL, VanillaMaterialKeys.WOOD, Items.WOODEN_SHOVEL)
        event.registerTool(CommonToolTypes.PICKAXE, VanillaMaterialKeys.WOOD, Items.WOODEN_PICKAXE)
        event.registerTool(CommonToolTypes.AXE, VanillaMaterialKeys.WOOD, Items.WOODEN_AXE)
        event.registerTool(CommonToolTypes.HOE, VanillaMaterialKeys.WOOD, Items.WOODEN_HOE)
        event.registerTool(CommonToolTypes.SWORD, VanillaMaterialKeys.WOOD, Items.WOODEN_SWORD)
        // Stone
        event.registerTool(CommonToolTypes.SHOVEL, VanillaMaterialKeys.STONE, Items.STONE_SHOVEL)
        event.registerTool(CommonToolTypes.PICKAXE, VanillaMaterialKeys.STONE, Items.STONE_PICKAXE)
        event.registerTool(CommonToolTypes.AXE, VanillaMaterialKeys.STONE, Items.STONE_AXE)
        event.registerTool(CommonToolTypes.HOE, VanillaMaterialKeys.STONE, Items.STONE_HOE)
        event.registerTool(CommonToolTypes.SWORD, VanillaMaterialKeys.STONE, Items.STONE_SWORD)
        // Iron
        event.registerTool(CommonToolTypes.SHOVEL, VanillaMaterialKeys.IRON, Items.IRON_SHOVEL)
        event.registerTool(CommonToolTypes.PICKAXE, VanillaMaterialKeys.IRON, Items.IRON_PICKAXE)
        event.registerTool(CommonToolTypes.AXE, VanillaMaterialKeys.IRON, Items.IRON_AXE)
        event.registerTool(CommonToolTypes.HOE, VanillaMaterialKeys.IRON, Items.IRON_HOE)
        event.registerTool(CommonToolTypes.SWORD, VanillaMaterialKeys.IRON, Items.IRON_SWORD)
        // Golden
        event.registerTool(CommonToolTypes.SHOVEL, VanillaMaterialKeys.GOLD, Items.GOLDEN_SHOVEL)
        event.registerTool(CommonToolTypes.PICKAXE, VanillaMaterialKeys.GOLD, Items.GOLDEN_PICKAXE)
        event.registerTool(CommonToolTypes.AXE, VanillaMaterialKeys.GOLD, Items.GOLDEN_AXE)
        event.registerTool(CommonToolTypes.HOE, VanillaMaterialKeys.GOLD, Items.GOLDEN_HOE)
        event.registerTool(CommonToolTypes.SWORD, VanillaMaterialKeys.GOLD, Items.GOLDEN_SWORD)
        // Diamond
        event.registerTool(CommonToolTypes.SHOVEL, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_SHOVEL)
        event.registerTool(CommonToolTypes.PICKAXE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_PICKAXE)
        event.registerTool(CommonToolTypes.AXE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_AXE)
        event.registerTool(CommonToolTypes.HOE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_HOE)
        event.registerTool(CommonToolTypes.SWORD, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_SWORD)
        // Netherite
        event.registerTool(CommonToolTypes.SHOVEL, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SHOVEL)
        event.registerTool(CommonToolTypes.PICKAXE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_PICKAXE)
        event.registerTool(CommonToolTypes.AXE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_AXE)
        event.registerTool(CommonToolTypes.HOE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_HOE)
        event.registerTool(CommonToolTypes.SWORD, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SWORD)
    }
}
