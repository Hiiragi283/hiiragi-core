package hiiragi283.core.common.plugin

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.item.tool.VanillaToolTypes
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.toBlockLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

@HTPlugin
object VanillaMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = vanillaId("material_plugin")

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        @Suppress("DEPRECATION")
        fun accept(part: HTPartLike, key: HTMaterialKey, block: Block) {
            consumer.accept(part, key, block.builtInRegistryHolder().toBlockLike())
        }
        // Fuels
        accept(CommonParts.ORE, VanillaMaterialKeys.COAL, Blocks.COAL_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.COAL, Blocks.DEEPSLATE_COAL_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.COAL, Blocks.COAL_BLOCK)
        // Mineral
        accept(CommonParts.ORE, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_BLOCK)

        accept(CommonParts.BLOCK, VanillaMaterialKeys.GLOWSTONE, Blocks.GLOWSTONE)
        // Gem
        accept(CommonParts.ORE, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.LAPIS, Blocks.DEEPSLATE_LAPIS_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_BLOCK)

        accept(CommonParts.ORE_NETHER, VanillaMaterialKeys.QUARTZ, Blocks.NETHER_QUARTZ_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.QUARTZ, Blocks.QUARTZ_BLOCK)

        accept(CommonParts.BLOCK, VanillaMaterialKeys.AMETHYST, Blocks.AMETHYST_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.EMERALD, Blocks.DEEPSLATE_EMERALD_ORE)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_BLOCK)
        // Metal
        accept(CommonParts.ORE, VanillaMaterialKeys.COPPER, Blocks.COPPER_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.COPPER, Blocks.DEEPSLATE_COPPER_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.COPPER, Blocks.RAW_COPPER_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.COPPER, Blocks.COPPER_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.IRON, Blocks.IRON_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.IRON, Blocks.DEEPSLATE_IRON_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.IRON, Blocks.RAW_IRON_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.IRON, Blocks.IRON_BLOCK)

        accept(CommonParts.ORE, VanillaMaterialKeys.GOLD, Blocks.GOLD_ORE)
        accept(CommonParts.ORE_DEEPSLATE, VanillaMaterialKeys.GOLD, Blocks.DEEPSLATE_GOLD_ORE)
        accept(CommonParts.ORE_NETHER, VanillaMaterialKeys.GOLD, Blocks.NETHER_GOLD_ORE)
        accept(CommonParts.RAW_BLOCK, VanillaMaterialKeys.GOLD, Blocks.RAW_GOLD_BLOCK)
        accept(CommonParts.BLOCK, VanillaMaterialKeys.GOLD, Blocks.GOLD_BLOCK)
        // Alloy
        accept(CommonParts.BLOCK, VanillaMaterialKeys.NETHERITE, Blocks.NETHERITE_BLOCK)
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        fun accept(part: HTPartLike, key: HTMaterialKey, item: Item) {
            consumer.accept(part, key, item.toLike())
        }

        // Fuel
        accept(CommonParts.FUEL, VanillaMaterialKeys.COAL, Items.COAL)
        accept(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, Items.CHARCOAL)
        // Mineral
        accept(CommonParts.DUST, VanillaMaterialKeys.REDSTONE, Items.REDSTONE)
        accept(CommonParts.DUST, VanillaMaterialKeys.GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        accept(CommonParts.GEM, VanillaMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        accept(CommonParts.GEM, VanillaMaterialKeys.QUARTZ, Items.QUARTZ)
        accept(CommonParts.GEM, VanillaMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        accept(CommonParts.GEM, VanillaMaterialKeys.DIAMOND, Items.DIAMOND)
        accept(CommonParts.GEM, VanillaMaterialKeys.EMERALD, Items.EMERALD)
        accept(CommonParts.GEM, VanillaMaterialKeys.ECHO, Items.ECHO_SHARD)
        accept(CommonParts.DUST, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_SHARD)
        accept(CommonParts.GEM, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_CRYSTALS)
        // Pearl
        accept(CommonParts.PEARL, VanillaMaterialKeys.ENDER, Items.ENDER_PEARL)
        // Metal
        accept(CommonParts.RAW, VanillaMaterialKeys.COPPER, Items.RAW_COPPER)
        accept(CommonParts.INGOT, VanillaMaterialKeys.COPPER, Items.COPPER_INGOT)

        accept(CommonParts.RAW, VanillaMaterialKeys.IRON, Items.RAW_IRON)
        accept(CommonParts.INGOT, VanillaMaterialKeys.IRON, Items.IRON_INGOT)
        accept(CommonParts.NUGGET, VanillaMaterialKeys.IRON, Items.IRON_NUGGET)

        accept(CommonParts.RAW, VanillaMaterialKeys.GOLD, Items.RAW_GOLD)
        accept(CommonParts.INGOT, VanillaMaterialKeys.GOLD, Items.GOLD_INGOT)
        accept(CommonParts.NUGGET, VanillaMaterialKeys.GOLD, Items.GOLD_NUGGET)
        // Alloy
        accept(CommonParts.SCRAP, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SCRAP)
        accept(CommonParts.INGOT, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)
        // Other
        accept(CommonParts.DUST, VanillaMaterialKeys.BLAZE, Items.BLAZE_POWDER)
        accept(CommonParts.ROD, VanillaMaterialKeys.BLAZE, Items.BLAZE_ROD)

        accept(CommonParts.DUST, VanillaMaterialKeys.BREEZE, Items.WIND_CHARGE)
        accept(CommonParts.ROD, VanillaMaterialKeys.BREEZE, Items.BREEZE_ROD)
    }

    override fun registerExistingTool(consumer: HTMaterialPlugin.ToolConsumer) {
        fun accept(toolType: HTToolType, key: HTMaterialKey, item: Item) {
            consumer.accept(toolType, key, item.toLike())
        }

        // Wooden
        accept(VanillaToolTypes.SHOVEL, VanillaMaterialKeys.WOOD, Items.WOODEN_SHOVEL)
        accept(VanillaToolTypes.PICKAXE, VanillaMaterialKeys.WOOD, Items.WOODEN_PICKAXE)
        accept(VanillaToolTypes.AXE, VanillaMaterialKeys.WOOD, Items.WOODEN_AXE)
        accept(VanillaToolTypes.HOE, VanillaMaterialKeys.WOOD, Items.WOODEN_HOE)
        accept(VanillaToolTypes.SWORD, VanillaMaterialKeys.WOOD, Items.WOODEN_SWORD)
        // Stone
        accept(VanillaToolTypes.SHOVEL, VanillaMaterialKeys.STONE, Items.STONE_SHOVEL)
        accept(VanillaToolTypes.PICKAXE, VanillaMaterialKeys.STONE, Items.STONE_PICKAXE)
        accept(VanillaToolTypes.AXE, VanillaMaterialKeys.STONE, Items.STONE_AXE)
        accept(VanillaToolTypes.HOE, VanillaMaterialKeys.STONE, Items.STONE_HOE)
        accept(VanillaToolTypes.SWORD, VanillaMaterialKeys.STONE, Items.STONE_SWORD)
        // Iron
        accept(VanillaToolTypes.SHOVEL, VanillaMaterialKeys.IRON, Items.IRON_SHOVEL)
        accept(VanillaToolTypes.PICKAXE, VanillaMaterialKeys.IRON, Items.IRON_PICKAXE)
        accept(VanillaToolTypes.AXE, VanillaMaterialKeys.IRON, Items.IRON_AXE)
        accept(VanillaToolTypes.HOE, VanillaMaterialKeys.IRON, Items.IRON_HOE)
        accept(VanillaToolTypes.SWORD, VanillaMaterialKeys.IRON, Items.IRON_SWORD)
        // Golden
        accept(VanillaToolTypes.SHOVEL, VanillaMaterialKeys.GOLD, Items.GOLDEN_SHOVEL)
        accept(VanillaToolTypes.PICKAXE, VanillaMaterialKeys.GOLD, Items.GOLDEN_PICKAXE)
        accept(VanillaToolTypes.AXE, VanillaMaterialKeys.GOLD, Items.GOLDEN_AXE)
        accept(VanillaToolTypes.HOE, VanillaMaterialKeys.GOLD, Items.GOLDEN_HOE)
        accept(VanillaToolTypes.SWORD, VanillaMaterialKeys.GOLD, Items.GOLDEN_SWORD)
        // Diamond
        accept(VanillaToolTypes.SHOVEL, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_SHOVEL)
        accept(VanillaToolTypes.PICKAXE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_PICKAXE)
        accept(VanillaToolTypes.AXE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_AXE)
        accept(VanillaToolTypes.HOE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_HOE)
        accept(VanillaToolTypes.SWORD, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_SWORD)
        // Netherite
        accept(VanillaToolTypes.SHOVEL, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SHOVEL)
        accept(VanillaToolTypes.PICKAXE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_PICKAXE)
        accept(VanillaToolTypes.AXE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_AXE)
        accept(VanillaToolTypes.HOE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_HOE)
        accept(VanillaToolTypes.SWORD, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SWORD)
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        fuel(provider)
        mineral(provider)
        gem(provider)
        metal(provider)
        pearl(provider)
        alloy(provider)
        other(provider)
    }

    @JvmStatic
    private fun fuel(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(VanillaMaterialKeys.COAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addItemPrefixes(
                CommonParts.DUST,
                CommonParts.RAW,
                CommonParts.CRUSHED_ORE,
                CommonParts.TINY,
            )
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    crushOre(CommonMaterialKeys.SULFUR, 1 / 4f)
                    crushCrushed(CommonMaterialKeys.CARBON, 1 / 4f)
                    washCrushed(VanillaMaterialKeys.DIAMOND, 1 / 8f)
                },
            )
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Coal", "石炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
        builder.getBuilder(VanillaMaterialKeys.CHARCOAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST, CommonParts.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Charcoal", "木炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
    }

    @JvmStatic
    private fun mineral(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(VanillaMaterialKeys.REDSTONE).apply {
            addItemPrefixes(CommonParts.RAW, CommonParts.CRUSHED_ORE)
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.CINNABAR, 1 / 4f)
                },
            )
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(4))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Redstone", "赤石")
        }
        builder.getBuilder(VanillaMaterialKeys.GLOWSTONE).apply {
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Glowstone", "グロウストーン")
        }
    }

    @JvmStatic
    private fun gem(builder: HTMaterialPlugin.MaterialProvider) {
        val itemSet: Set<HTPartLike> = setOf(CommonParts.DUST, CommonParts.RAW, CommonParts.CRUSHED_ORE)
        builder.getBuilder(VanillaMaterialKeys.LAPIS).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet)
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.SALTPETER, 1 / 4f)
                },
            )
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(4))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Lapis", "ラピス")
            setTextureSet("lapis")
        }
        builder.getBuilder(VanillaMaterialKeys.QUARTZ).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet)
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.SULFUR, 1 / 4f)
                },
            )
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Quartz", "水晶")
            setTextureSet("quartz", HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(VanillaMaterialKeys.AMETHYST).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Amethyst", "アメジスト")
            setTextureSet("amethyst")
        }
        builder.getBuilder(VanillaMaterialKeys.DIAMOND).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet.plus(CommonParts.GEAR))
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.CARBON, 1 / 4f)
                },
            )
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Diamond", "ダイヤモンド")
            setTextureSet("diamond")
        }
        builder.getBuilder(VanillaMaterialKeys.EMERALD).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet.plus(CommonParts.GEAR))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Emerald", "エメラルド")
            setTextureSet("emerald")
        }
        builder.getBuilder(VanillaMaterialKeys.ECHO).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Echo Shard", "残響の欠片")
            setTextureSet("echo")
        }
        builder.getBuilder(VanillaMaterialKeys.PRISMARINE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Prismarine", "プリズマリン")
        }
    }

    @JvmStatic
    private fun pearl(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(VanillaMaterialKeys.ENDER).apply {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Ender Pearl", "エンダーパール")
            setTextureSet("pearl")
        }
    }

    @JvmStatic
    private fun metal(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(VanillaMaterialKeys.COPPER).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonParts.DUST,
                CommonParts.CRUSHED_ORE,
                CommonParts.GEAR,
                CommonParts.NUGGET,
                CommonParts.PLATE,
                CommonParts.ROD,
                CommonParts.WIRE,
            )
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    crushOre(CommonMaterialKeys.NICKEL, 1 / 4f)
                    crushCrushed(VanillaMaterialKeys.GOLD, 1 / 4f)
                    washCrushed(VanillaMaterialKeys.GOLD, 1 / 2f)
                },
            )
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3, 2))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Copper", "銅")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(VanillaMaterialKeys.IRON).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonParts.DUST,
                CommonParts.CRUSHED_ORE,
                CommonParts.GEAR,
                CommonParts.PLATE,
                CommonParts.ROD,
            )
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    crushOre(CommonMaterialKeys.TIN, 1 / 4f)
                    crushCrushed(CommonMaterialKeys.NICKEL, 1 / 4f)
                    washCrushed(CommonMaterialKeys.NICKEL, 1 / 2f)
                },
            )
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Iron", "鉄")
        }
        builder.getBuilder(VanillaMaterialKeys.GOLD).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonParts.DUST,
                CommonParts.CRUSHED_ORE,
                CommonParts.GEAR,
                CommonParts.PLATE,
                CommonParts.ROD,
                CommonParts.WIRE,
            )
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    crushOre(VanillaMaterialKeys.COPPER, 1 / 4f)
                    crushCrushed(CommonMaterialKeys.SILVER, 1 / 4f)
                    washCrushed(CommonMaterialKeys.SILVER, 1 / 2f)
                },
            )
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Gold", "金")
        }
    }

    @JvmStatic
    private fun alloy(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(VanillaMaterialKeys.NETHERITE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonParts.DUST,
                CommonParts.GEAR,
                CommonParts.NUGGET,
                CommonParts.PLATE,
                CommonParts.ROD,
            )
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Netherite", "ネザライト")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(
                HTMaterialPropertyKeys.SMITHING_RECIPE,
                HTSmithingRecipeProperty(
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.toLike(),
                    VanillaMaterialKeys.DIAMOND,
                    false,
                ),
            )
        }
    }

    @JvmStatic
    private fun other(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(VanillaMaterialKeys.WOOD).apply {
            setDefaultPart(ItemTags.PLANKS, Items.OAK_PLANKS.toLike())
            addItemPrefixes(CommonParts.DUST, CommonParts.GEAR)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Wood", "木")
            addCustomName(CommonParts.DUST, "Sawdust", "おがくず")
            setTextureSet("mineral")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
        }
        builder.getBuilder(VanillaMaterialKeys.GLASS).apply {
            setDefaultPart(Tags.Items.GLASS_BLOCKS, Items.GLASS.toLike())
            addItemPrefixes(CommonParts.DUST, CommonParts.ROD)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Glass", "ガラス")
            setTextureSet(HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("white"))
        }
        builder.getBuilder(VanillaMaterialKeys.STONE).apply {
            setDefaultPart(ItemTags.STONE_CRAFTING_MATERIALS, Items.COBBLESTONE.toLike())
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Stone", "石")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        builder.getBuilder(VanillaMaterialKeys.OBSIDIAN).apply {
            setDefaultPart(Tags.Items.OBSIDIANS_NORMAL, Items.OBSIDIAN.toLike())
            addItemPrefixes(CommonParts.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(4))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Obsidian", "黒曜石")
            setTextureSet(HTMaterialTextureSet.DULL)
        }

        builder.getBuilder(VanillaMaterialKeys.BLAZE).apply {
            setDefaultPart(Tags.Items.RODS_BLAZE, Items.BLAZE_ROD.toLike())
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(4))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Blaze", "ブレイズ")
        }
        builder.getBuilder(VanillaMaterialKeys.BREEZE).apply {
            setDefaultPart(Tags.Items.RODS_BREEZE, Items.BREEZE_ROD.toLike())
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(6))
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Breeze", "ブリーズ")
        }

        builder.getBuilder(VanillaMaterialKeys.BRICK).apply {
            setDefaultPart(Tags.Items.BRICKS_NORMAL, Items.BRICK.toLike())
            addItemPrefixes(CommonParts.DUST, CommonParts.PLATE)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Brick", "レンガ")
        }
        builder.getBuilder(VanillaMaterialKeys.NETHER_BRICK).apply {
            setDefaultPart(Tags.Items.BRICKS_NETHER, Items.NETHER_BRICK.toLike())
            addItemPrefixes(CommonParts.DUST, CommonParts.PLATE)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HTConst.MINECRAFT)

            setName("Nether Brick", "ネザーレンガ")
        }
    }
}
