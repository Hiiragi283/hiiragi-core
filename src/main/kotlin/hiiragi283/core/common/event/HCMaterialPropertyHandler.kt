package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.material.HTMaterialKey
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
import hiiragi283.core.api.material.property.addToolPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.setup.HCToolMaterials
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCMaterialPropertyHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun gatherProperties(event: HTMaterialPropertyEvent) {
        vanilla(event)
        common(event)
        hiiragiCore(event)
    }

    private val materialBlockSet: Set<HTTagPrefix> = buildSet {
        addAll(CommonTagPrefixes.ORES)
        add(CommonTagPrefixes.RAW_BLOCK)
        add(CommonTagPrefixes.BLOCK)
    }

    private val oreSet: Set<HTTagPrefix> = setOf(
        CommonTagPrefixes.DUST,
        CommonTagPrefixes.RAW,
        CommonTagPrefixes.CRUSHED_ORE,
    )
    private val gemSet: Set<HTTagPrefix> = oreSet.plus(CommonTagPrefixes.GEM)
    private val metalSet: Set<HTTagPrefix> = oreSet.plus(CommonTagPrefixes.INGOT).plus(CommonTagPrefixes.NUGGET)
    private val alloySet: Set<HTTagPrefix> = metalSet.minus(CommonTagPrefixes.RAW).minus(CommonTagPrefixes.CRUSHED_ORE)
    private val partSet: Set<HTTagPrefix> = setOf(CommonTagPrefixes.GEAR, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)

    @JvmStatic
    private fun vanilla(event: HTMaterialPropertyEvent) {
        // Fuels
        event.modify(VanillaMaterialKeys.COAL) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addItemPrefixes(oreSet.plus(CommonTagPrefixes.TINY))
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
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)

            setName("Charcoal", "木炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
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
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Glowstone", "グロウストーン")
        }
        event.modify(VanillaMaterialKeys.CALCITE) {
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Calcite", "方解石")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("white"))
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(oreSet)
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
        event.modify(VanillaMaterialKeys.QUARTZ) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(oreSet)
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
        event.modify(VanillaMaterialKeys.AMETHYST) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(oreSet)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Amethyst", "アメジスト")
            setTextureSet("amethyst")
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.RAW,
                CommonTagPrefixes.CRUSHED_ORE,
                CommonTagPrefixes.GEAR,
            )
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    all(CommonMaterialKeys.CARBON, 1 / 4f)
                },
            )

            setName("Diamond", "ダイヤモンド")
            setTextureSet("diamond")
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.RAW,
                CommonTagPrefixes.CRUSHED_ORE,
                CommonTagPrefixes.GEAR,
            )

            setName("Emerald", "エメラルド")
            setTextureSet("emerald")
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Echo Shard", "残響の欠片")
            setTextureSet("echo")
        }
        event.modify(VanillaMaterialKeys.PRISMARINE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)

            setName("Prismarine", "プリズマリン")
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Ender Pearl", "エンダーパール")
            setTextureSet("pearl")
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
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
        event.modify(VanillaMaterialKeys.IRON) {
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
        event.modify(VanillaMaterialKeys.GOLD) {
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
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
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
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            setDefaultPart(ItemTags.PLANKS, HTItemHolderLike.of(Items.OAK_PLANKS))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Wood", "木")
            addCustomName(CommonTagPrefixes.DUST, "Sawdust", "おがくず")
            setTextureSet("mineral")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
        }
        event.modify(VanillaMaterialKeys.GLASS) {
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
        event.modify(VanillaMaterialKeys.STONE) {
            setDefaultPart(ItemTags.STONE_CRAFTING_MATERIALS, HTItemHolderLike.of(Items.COBBLESTONE))

            setName("Stone", "石")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            setDefaultPart(Tags.Items.OBSIDIANS_NORMAL, HTItemHolderLike.of(Items.OBSIDIAN))
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(4))

            setName("Obsidian", "黒曜石")
            setTextureSet(HTMaterialTextureSet.DULL)
        }

        event.modify(VanillaMaterialKeys.BLAZE) {
            setDefaultPart(Tags.Items.RODS_BLAZE, HTItemHolderLike.of(Items.BLAZE_ROD))
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(4))

            setName("Blaze", "ブレイズ")
        }
        event.modify(VanillaMaterialKeys.BREEZE) {
            setDefaultPart(Tags.Items.RODS_BREEZE, HTItemHolderLike.of(Items.BREEZE_ROD))
            put(HTMaterialPropertyKeys.DEFAULT_SCALE, fraction(6))

            setName("Breeze", "ブリーズ")
        }

        event.modify(VanillaMaterialKeys.BRICK) {
            setDefaultPart(Tags.Items.BRICKS_NORMAL, HTItemHolderLike.of(Items.BRICK))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)

            setName("Brick", "レンガ")
        }
        event.modify(VanillaMaterialKeys.NETHER_BRICK) {
            setDefaultPart(Tags.Items.BRICKS_NETHER, HTItemHolderLike.of(Items.NETHER_BRICK))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)

            setName("Nether Brick", "ネザーレンガ")
        }
    }

    @JvmStatic
    private fun common(event: HTMaterialPropertyEvent) {
        fun registerGem(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                setDefaultPart(HTDefaultPart.Prefixed.GEM)

                setName(enName, jaName)
            }
        }

        fun registerMetal(
            key: HTMaterialKey,
            enName: String,
            jaName: String,
            level: HTMaterialLevel = HTMaterialLevel.MEDIUM,
        ) {
            event.modify(key) {
                setDefaultPart(HTDefaultPart.Prefixed.INGOT)
                put(HTMaterialPropertyKeys.HARDNESS, level)
                put(HTMaterialPropertyKeys.MELTING_POINT, level)

                setName(enName, jaName)
            }
        }

        fun registerMineral(
            key: HTMaterialKey,
            enName: String,
            jaName: String,
            builderAction: HTPropertyMap.Mutable.() -> Unit = {},
        ) {
            event.modify(key) {
                setDefaultPart(HTDefaultPart.Prefixed.GEM)
                addBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK))
                addItemPrefixes(oreSet)

                setName(enName, jaName)
                setTextureSet("mineral", HTMaterialTextureSet.DULL)
                builderAction()
            }
        }

        fun platinumGroup(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                setDefaultPart(HTDefaultPart.Prefixed.INGOT)
                addBlockPrefixes(CommonTagPrefixes.BLOCK)
                addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
                addItemPrefixes(alloySet)
                put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGHEST)
                put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGHEST)

                setName(enName, jaName)
                setTextureSet(HTMaterialTextureSet.MYSTICAL)
            }
        }
        // Fuels
        event.modify(CommonMaterialKeys.COAL_COKE) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL, CommonTagPrefixes.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)

            setName("Coal Coke", "石炭コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        // Minerals
        registerMineral(CommonMaterialKeys.SALT, "Salt", "塩")
        registerMineral(CommonMaterialKeys.SALTPETER, "Saltpeter", "硝石")
        registerMineral(CommonMaterialKeys.BAUXITE, "Bauxite", "ボーキサイト")

        registerMineral(CommonMaterialKeys.SULFUR, "Sulfur", "硫黄")

        registerMineral(CommonMaterialKeys.PLATINUM_GROUP, "Platinum Group", "白金族") {
            setTextureSet("mineral", HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("black"))
        }

        registerMineral(CommonMaterialKeys.CINNABAR, "Cinnabar", "辰砂")

        registerMineral(CommonMaterialKeys.GALENA, "Galena", "方鉛鉱") {
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    crushOre(CommonMaterialKeys.SULFUR, 1 / 4f)
                    crushCrushed(CommonMaterialKeys.SILVER, 1 / 4f)
                    washCrushed(CommonMaterialKeys.SILVER, 1 / 2f)
                },
            )
            put(HTMaterialPropertyKeys.SMELTED_TO, CommonMaterialKeys.LEAD)
        }
        // Gems
        event.modify(CommonMaterialKeys.FLUORITE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

            setName("Fluorite", "蛍石")
        }
        registerGem(CommonMaterialKeys.PERIDOT, "Peridot", "ペリドット")
        registerGem(CommonMaterialKeys.RUBY, "Ruby", "ルビー")
        registerGem(CommonMaterialKeys.SAPPHIRE, "Sapphire", "サファイア")
        // Metals
        registerMetal(CommonMaterialKeys.LITHIUM, "Lithium", "リチウム", HTMaterialLevel.LOW)
        registerMetal(CommonMaterialKeys.BERYLLIUM, "Beryllium", "ベリリウム", HTMaterialLevel.HIGH)

        registerMetal(CommonMaterialKeys.SODIUM, "Sodium", "ナトリウム", HTMaterialLevel.LOW)
        registerMetal(CommonMaterialKeys.MAGNESIUM, "Magnesium", "マグネシウム")

        event.modify(CommonMaterialKeys.ALUMINUM) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(alloySet.plus(CommonTagPrefixes.WIRE))
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Aluminum", "アルミニウム")
        }
        event.modify(CommonMaterialKeys.SILICON) {
            setDefaultPart(HiiragiCoreTags.Items.SILICON, null)

            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGH)

            setName("Silicon", "シリコン")
        }

        registerMetal(CommonMaterialKeys.TITANIUM, "Titanium", "チタン", HTMaterialLevel.HIGH)
        registerMetal(CommonMaterialKeys.VANADIUM, "Vanadium", "バナジウム")
        registerMetal(CommonMaterialKeys.CHROMIUM, "Chromium", "クロム")
        registerMetal(CommonMaterialKeys.MANGANESE, "Manganese", "マンガン")
        registerMetal(CommonMaterialKeys.COBALT, "Cobalt", "コバルト")
        event.modify(CommonMaterialKeys.NICKEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Nickel", "ニッケル")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        event.modify(CommonMaterialKeys.ZINC) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(metalSet)

            setName("Zinc", "亜鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
        }

        registerMetal(CommonMaterialKeys.MOLYBDENUM, "Molybdenum", "モリブデン")
        platinumGroup(CommonMaterialKeys.RUTHENIUM, "Ruthenium", "ルテニウム")
        platinumGroup(CommonMaterialKeys.RHODIUM, "Rhodium", "ロジウム")
        platinumGroup(CommonMaterialKeys.PALLADIUM, "Palladium", "パラジウム")
        event.modify(CommonMaterialKeys.SILVER) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Silver", "銀")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        event.modify(CommonMaterialKeys.TIN) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(metalSet)

            setName("Tin", "錫")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        registerMetal(CommonMaterialKeys.ANTIMONY, "Antimony", "アンチモン")

        registerMetal(CommonMaterialKeys.TUNGSTEN, "Tungsten", "パラジウム", HTMaterialLevel.HIGH)
        platinumGroup(CommonMaterialKeys.OSMIUM, "Osmium", "オスミウム")
        platinumGroup(CommonMaterialKeys.IRIDIUM, "Iridium", "イリジウム")
        platinumGroup(CommonMaterialKeys.PLATINUM, "Platinum", "白金")
        event.modify(CommonMaterialKeys.LEAD) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Lead", "鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
        }

        registerMetal(CommonMaterialKeys.URANIUM, "Uranium", "ウラン")
        registerMetal(CommonMaterialKeys.PLUTONIUM, "Plutonium", "プルトニウム")
        // Alloys
        event.modify(CommonMaterialKeys.STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.STEEL, CommonToolTypes.VANILLA_SET)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Steel", "鋼鉄")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        registerMetal(CommonMaterialKeys.INVAR, "Invar", "不変鋼")

        event.modify(CommonMaterialKeys.BRASS) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Brass", "真鍮")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        registerMetal(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        event.modify(CommonMaterialKeys.BRONZE) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.BRONZE, CommonToolTypes.VANILLA_SET)

            setName("Bronze", "青銅")
        }

        registerMetal(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        registerMetal(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        registerMetal(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        registerMetal(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
        // Others
        event.modify(CommonMaterialKeys.ASH) {
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Ash", "灰")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        event.modify(CommonMaterialKeys.CARBON) {
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Carbon", "炭素")
            // addCustomName(CommonTagPrefixes.WIRE, "Carbon Fiber", "炭素繊維")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, VanillaMaterialKeys.COAL.getId())
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            setDefaultPart(
                HiiragiCoreTags.Items.PLASTICS,
                HTSimpleDeferredItem(CommonTagPrefixes.PLATE.createId(CommonMaterialKeys.PLASTIC)),
            )
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Plastic", "プラスチック")
            // addCustomName(CommonTagPrefixes.DUST, "Plastic Pulp", "プラスチックパルプ")
            addCustomName(CommonTagPrefixes.INGOT, "Plastic Bar", "プラスチックバー")
            addCustomName(CommonTagPrefixes.PLATE, "Plastic Sheet", "プラスチックシート")
            // addCustomName(CommonTagPrefixes.WIRE, "Synthetic Fiber", "合成繊維")
            setTextureSet("polymer")
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.INGOT, CommonTagPrefixes.PLATE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Rubber", "ゴム")
            // addCustomName(CommonTagPrefixes.DUST, "Rubber Pulp", "ゴムパルプ")
            addCustomName(CommonTagPrefixes.INGOT, "Rubber Bar", "ゴムバー")
            addCustomName(CommonTagPrefixes.PLATE, "Rubber Sheet", "ゴムシート")
            setTextureSet("polymer", HTMaterialTextureSet.DULL)
        }
    }

    @JvmStatic
    private fun hiiragiCore(event: HTMaterialPropertyEvent) {
        // Gems
        event.modify(HCMaterialKeys.AZURE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Azure Shard", "紺碧の欠片")
            setTextureSet("amethyst", HTMaterialTextureSet.SHINE)
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)

            setName("Crimson Crystal", "深紅のクリスタリル")
            setTextureSet("emerald")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 24)
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)

            setName("Warped Crystal", "歪んだクリスタリル")
            setTextureSet("emerald")
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PEARL)

            setName("Eldritch Pearl", "異質な真珠")
            setTextureSet("pearl", HTMaterialTextureSet.MYSTICAL)
        }
        // Metals
        /*event.modify(HCMaterialKeys.ANCIENT_METAL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.SCRAP,
                CommonTagPrefixes.INGOT,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )
            addToolPrefixes(HCToolMaterials.ANCIENT_METAL, CommonToolTypes.VANILLA_SET))
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Ancient Metal", "古代の金属")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(
                HTMaterialPropertyKeys.SMITHING_RECIPE,
                HTSmithingRecipeProperty(
                    HCItems.ANCIENT_UPGRADE,
                    VanillaMaterialKeys.DIAMOND,
                    false,
                ),
            )
        }
        event.modify(HCMaterialKeys.OMINOUS_METAL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Ominous Metal", "不吉な金属")
        }*/
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.AZURE_STEEL, CommonToolTypes.VANILLA_SET)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Azure Steel", "紺碧鋼")
        }
    }
}
