package hiiragi283.core.common.plugin

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addFluidPrefixes
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
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

    override fun getId(): ResourceLocation = HTConst.MINECRAFT.toId("material_plugin")

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        fun accept(prefix: HTTagPrefix, material: HTMaterialLike, block: Block) {
            consumer.accept(prefix, material.asMaterialKey(), block.toLike())
        }

        // Fuels
        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.COAL, Blocks.COAL_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.COAL, Blocks.DEEPSLATE_COAL_ORE)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL, Blocks.COAL_BLOCK)
        // Mineral
        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.REDSTONE, Blocks.DEEPSLATE_REDSTONE_ORE)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.REDSTONE, Blocks.REDSTONE_BLOCK)

        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.GLOWSTONE, Blocks.GLOWSTONE)
        // Gem
        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.LAPIS, Blocks.DEEPSLATE_LAPIS_ORE)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.LAPIS, Blocks.LAPIS_BLOCK)

        accept(CommonTagPrefixes.ORE_NETHER, VanillaMaterialKeys.QUARTZ, Blocks.NETHER_QUARTZ_ORE)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.QUARTZ, Blocks.QUARTZ_BLOCK)

        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.AMETHYST, Blocks.AMETHYST_BLOCK)

        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.DIAMOND, Blocks.DEEPSLATE_DIAMOND_ORE)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.DIAMOND, Blocks.DIAMOND_BLOCK)

        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.EMERALD, Blocks.DEEPSLATE_EMERALD_ORE)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.EMERALD, Blocks.EMERALD_BLOCK)
        // Metal
        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.COPPER, Blocks.COPPER_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.COPPER, Blocks.DEEPSLATE_COPPER_ORE)
        accept(CommonTagPrefixes.RAW_BLOCK, VanillaMaterialKeys.COPPER, Blocks.RAW_COPPER_BLOCK)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COPPER, Blocks.COPPER_BLOCK)

        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.IRON, Blocks.IRON_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.IRON, Blocks.DEEPSLATE_IRON_ORE)
        accept(CommonTagPrefixes.RAW_BLOCK, VanillaMaterialKeys.IRON, Blocks.RAW_IRON_BLOCK)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.IRON, Blocks.IRON_BLOCK)

        accept(CommonTagPrefixes.ORE, VanillaMaterialKeys.GOLD, Blocks.GOLD_ORE)
        accept(CommonTagPrefixes.ORE_DEEPSLATE, VanillaMaterialKeys.GOLD, Blocks.DEEPSLATE_GOLD_ORE)
        accept(CommonTagPrefixes.ORE_NETHER, VanillaMaterialKeys.GOLD, Blocks.NETHER_GOLD_ORE)
        accept(CommonTagPrefixes.RAW_BLOCK, VanillaMaterialKeys.GOLD, Blocks.RAW_GOLD_BLOCK)
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.GOLD, Blocks.GOLD_BLOCK)
        // Alloy
        accept(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.NETHERITE, Blocks.NETHERITE_BLOCK)
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        fun accept(prefix: HTTagPrefix, material: HTMaterialLike, item: Item) {
            consumer.accept(prefix, material.asMaterialKey(), HTItemHolderLike.of(item))
        }

        // Fuel
        accept(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, Items.COAL)
        accept(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, Items.CHARCOAL)
        // Mineral
        accept(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, Items.REDSTONE)
        accept(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.LAPIS, Items.LAPIS_LAZULI)
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ, Items.QUARTZ)
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST, Items.AMETHYST_SHARD)
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND, Items.DIAMOND)
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD, Items.EMERALD)
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO, Items.ECHO_SHARD)
        accept(CommonTagPrefixes.DUST, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_SHARD)
        accept(CommonTagPrefixes.GEM, VanillaMaterialKeys.PRISMARINE, Items.PRISMARINE_CRYSTALS)
        // Pearl
        accept(CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER, Items.ENDER_PEARL)
        // Metal
        accept(CommonTagPrefixes.RAW, VanillaMaterialKeys.COPPER, Items.RAW_COPPER)
        accept(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER, Items.COPPER_INGOT)

        accept(CommonTagPrefixes.RAW, VanillaMaterialKeys.IRON, Items.RAW_IRON)
        accept(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, Items.IRON_INGOT)
        accept(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON, Items.IRON_NUGGET)

        accept(CommonTagPrefixes.RAW, VanillaMaterialKeys.GOLD, Items.RAW_GOLD)
        accept(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD, Items.GOLD_INGOT)
        accept(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.GOLD, Items.GOLD_NUGGET)
        // Alloy
        accept(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SCRAP)
        accept(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_INGOT)
        // Other
        accept(CommonTagPrefixes.DUST, VanillaMaterialKeys.BLAZE, Items.BLAZE_POWDER)
        accept(CommonTagPrefixes.ROD, VanillaMaterialKeys.BLAZE, Items.BLAZE_ROD)

        accept(CommonTagPrefixes.DUST, VanillaMaterialKeys.BREEZE, Items.WIND_CHARGE)
        accept(CommonTagPrefixes.ROD, VanillaMaterialKeys.BREEZE, Items.BREEZE_ROD)
    }

    override fun registerExistingTool(consumer: HTMaterialPlugin.ToolConsumer) {
        fun accept(toolType: HTToolType, material: HTMaterialLike, item: Item) {
            consumer.accept(toolType, material.asMaterialKey(), HTItemHolderLike.of(item))
        }

        // Wooden
        accept(CommonToolTypes.SHOVEL, VanillaMaterialKeys.WOOD, Items.WOODEN_SHOVEL)
        accept(CommonToolTypes.PICKAXE, VanillaMaterialKeys.WOOD, Items.WOODEN_PICKAXE)
        accept(CommonToolTypes.AXE, VanillaMaterialKeys.WOOD, Items.WOODEN_AXE)
        accept(CommonToolTypes.HOE, VanillaMaterialKeys.WOOD, Items.WOODEN_HOE)
        accept(CommonToolTypes.SWORD, VanillaMaterialKeys.WOOD, Items.WOODEN_SWORD)
        // Stone
        accept(CommonToolTypes.SHOVEL, VanillaMaterialKeys.STONE, Items.STONE_SHOVEL)
        accept(CommonToolTypes.PICKAXE, VanillaMaterialKeys.STONE, Items.STONE_PICKAXE)
        accept(CommonToolTypes.AXE, VanillaMaterialKeys.STONE, Items.STONE_AXE)
        accept(CommonToolTypes.HOE, VanillaMaterialKeys.STONE, Items.STONE_HOE)
        accept(CommonToolTypes.SWORD, VanillaMaterialKeys.STONE, Items.STONE_SWORD)
        // Iron
        accept(CommonToolTypes.SHOVEL, VanillaMaterialKeys.IRON, Items.IRON_SHOVEL)
        accept(CommonToolTypes.PICKAXE, VanillaMaterialKeys.IRON, Items.IRON_PICKAXE)
        accept(CommonToolTypes.AXE, VanillaMaterialKeys.IRON, Items.IRON_AXE)
        accept(CommonToolTypes.HOE, VanillaMaterialKeys.IRON, Items.IRON_HOE)
        accept(CommonToolTypes.SWORD, VanillaMaterialKeys.IRON, Items.IRON_SWORD)
        // Golden
        accept(CommonToolTypes.SHOVEL, VanillaMaterialKeys.GOLD, Items.GOLDEN_SHOVEL)
        accept(CommonToolTypes.PICKAXE, VanillaMaterialKeys.GOLD, Items.GOLDEN_PICKAXE)
        accept(CommonToolTypes.AXE, VanillaMaterialKeys.GOLD, Items.GOLDEN_AXE)
        accept(CommonToolTypes.HOE, VanillaMaterialKeys.GOLD, Items.GOLDEN_HOE)
        accept(CommonToolTypes.SWORD, VanillaMaterialKeys.GOLD, Items.GOLDEN_SWORD)
        // Diamond
        accept(CommonToolTypes.SHOVEL, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_SHOVEL)
        accept(CommonToolTypes.PICKAXE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_PICKAXE)
        accept(CommonToolTypes.AXE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_AXE)
        accept(CommonToolTypes.HOE, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_HOE)
        accept(CommonToolTypes.SWORD, VanillaMaterialKeys.DIAMOND, Items.DIAMOND_SWORD)
        // Netherite
        accept(CommonToolTypes.SHOVEL, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SHOVEL)
        accept(CommonToolTypes.PICKAXE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_PICKAXE)
        accept(CommonToolTypes.AXE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_AXE)
        accept(CommonToolTypes.HOE, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_HOE)
        accept(CommonToolTypes.SWORD, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SWORD)
    }

    override fun onModifyMaterial(builder: HTMaterialPlugin.MaterialBuilder) {
        fuel(builder)
        mineral(builder)
        gem(builder)
        metal(builder)
        pearl(builder)
        alloy(builder)
        other(builder)
    }

    @JvmStatic
    private fun fuel(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(VanillaMaterialKeys.COAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.RAW,
                CommonTagPrefixes.CRUSHED_ORE,
                CommonTagPrefixes.TINY,
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

            setName("Coal", "石炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
        builder.getBuilder(VanillaMaterialKeys.CHARCOAL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)

            setName("Charcoal", "木炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
    }

    @JvmStatic
    private fun mineral(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(VanillaMaterialKeys.REDSTONE).apply {
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.RAW, CommonTagPrefixes.CRUSHED_ORE)
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.CINNABAR, 1 / 4f)
                },
            )
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(4))

            setName("Redstone", "赤石")
        }
        builder.getBuilder(VanillaMaterialKeys.GLOWSTONE).apply {
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Glowstone", "グロウストーン")
        }
    }

    @JvmStatic
    private fun gem(builder: HTMaterialPlugin.MaterialBuilder) {
        val itemSet: Set<HTTagPrefix> = setOf(CommonTagPrefixes.DUST, CommonTagPrefixes.RAW, CommonTagPrefixes.CRUSHED_ORE)
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

            setName("Quartz", "水晶")
            setTextureSet("quartz", HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(VanillaMaterialKeys.AMETHYST).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Amethyst", "アメジスト")
            setTextureSet("amethyst")
        }
        builder.getBuilder(VanillaMaterialKeys.DIAMOND).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet.plus(CommonTagPrefixes.GEAR))
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.CARBON, 1 / 4f)
                },
            )

            setName("Diamond", "ダイヤモンド")
            setTextureSet("diamond")
        }
        builder.getBuilder(VanillaMaterialKeys.EMERALD).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(itemSet.plus(CommonTagPrefixes.GEAR))

            setName("Emerald", "エメラルド")
            setTextureSet("emerald")
        }
        builder.getBuilder(VanillaMaterialKeys.ECHO).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Echo Shard", "残響の欠片")
            setTextureSet("echo")
        }
        builder.getBuilder(VanillaMaterialKeys.PRISMARINE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)

            setName("Prismarine", "プリズマリン")
        }
    }

    @JvmStatic
    private fun pearl(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(VanillaMaterialKeys.ENDER).apply {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Ender Pearl", "エンダーパール")
            setTextureSet("pearl")
        }
    }

    @JvmStatic
    private fun metal(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(VanillaMaterialKeys.COPPER).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.CRUSHED_ORE,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
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

            setName("Copper", "銅")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(VanillaMaterialKeys.IRON).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.CRUSHED_ORE,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
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

            setName("Iron", "鉄")
        }
        builder.getBuilder(VanillaMaterialKeys.GOLD).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.CRUSHED_ORE,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
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

            setName("Gold", "金")
        }
    }

    @JvmStatic
    private fun alloy(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(VanillaMaterialKeys.NETHERITE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGH)

            setName("Netherite", "ネザライト")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(
                HTMaterialPropertyKeys.SMITHING_RECIPE,
                HTSmithingRecipeProperty(
                    HTItemHolderLike.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                    VanillaMaterialKeys.DIAMOND,
                    false,
                ),
            )
        }
    }

    @JvmStatic
    private fun other(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(VanillaMaterialKeys.WOOD).apply {
            setDefaultPart(ItemTags.PLANKS, HTItemHolderLike.of(Items.OAK_PLANKS))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Wood", "木")
            addCustomName(CommonTagPrefixes.DUST, "Sawdust", "おがくず")
            setTextureSet("mineral")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
        }
        builder.getBuilder(VanillaMaterialKeys.GLASS).apply {
            setDefaultPart(Tags.Items.GLASS_BLOCKS, HTItemHolderLike.of(Items.GLASS))
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.ROD)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Glass", "ガラス")
            setTextureSet(HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("white"))
        }
        builder.getBuilder(VanillaMaterialKeys.STONE).apply {
            setDefaultPart(ItemTags.STONE_CRAFTING_MATERIALS, HTItemHolderLike.of(Items.COBBLESTONE))

            setName("Stone", "石")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        builder.getBuilder(VanillaMaterialKeys.OBSIDIAN).apply {
            setDefaultPart(Tags.Items.OBSIDIANS_NORMAL, HTItemHolderLike.of(Items.OBSIDIAN))
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(4))

            setName("Obsidian", "黒曜石")
            setTextureSet(HTMaterialTextureSet.DULL)
        }

        builder.getBuilder(VanillaMaterialKeys.BLAZE).apply {
            setDefaultPart(Tags.Items.RODS_BLAZE, HTItemHolderLike.of(Items.BLAZE_ROD))
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(4))

            setName("Blaze", "ブレイズ")
        }
        builder.getBuilder(VanillaMaterialKeys.BREEZE).apply {
            setDefaultPart(Tags.Items.RODS_BREEZE, HTItemHolderLike.of(Items.BREEZE_ROD))
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(6))

            setName("Breeze", "ブリーズ")
        }

        builder.getBuilder(VanillaMaterialKeys.BRICK).apply {
            setDefaultPart(Tags.Items.BRICKS_NORMAL, HTItemHolderLike.of(Items.BRICK))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PLATE)

            setName("Brick", "レンガ")
        }
        builder.getBuilder(VanillaMaterialKeys.NETHER_BRICK).apply {
            setDefaultPart(Tags.Items.BRICKS_NETHER, HTItemHolderLike.of(Items.NETHER_BRICK))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PLATE)

            setName("Nether Brick", "ネザーレンガ")
        }
    }
}
