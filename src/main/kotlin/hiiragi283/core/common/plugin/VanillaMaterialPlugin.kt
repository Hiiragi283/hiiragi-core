package hiiragi283.core.common.plugin

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
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
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

@HTPlugin
object VanillaMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = HTConst.MINECRAFT.toId("material_plugin")

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
