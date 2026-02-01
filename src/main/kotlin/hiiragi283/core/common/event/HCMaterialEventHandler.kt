package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTBlockLootFactory
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTFormingRecipeFlag
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmithingRecipeProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addCustomOreLoot
import hiiragi283.core.api.material.property.addExtraOreResult
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.addToolPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.item.VanillaEquipmentMaterial
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCToolMaterials
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCMaterialEventHandler {
    @SubscribeEvent
    fun gatherAttributes(event: HTMaterialPropertyEvent) {
        vanilla(event)
        common(event)
        hiiragiCore(event)
    }

    private val materialBlockSet: Set<HTTagPrefix> = buildSet {
        addAll(CommonTagPrefixes.ORES)
        add(CommonTagPrefixes.RAW_BLOCK)
        add(CommonTagPrefixes.BLOCK)
    }

    private val metalSet: Set<HTTagPrefix> = setOf(
        CommonTagPrefixes.DUST,
        CommonTagPrefixes.RAW,
        CommonTagPrefixes.INGOT,
        CommonTagPrefixes.NUGGET,
    )
    private val alloySet: Set<HTTagPrefix> = metalSet.minus(CommonTagPrefixes.RAW)
    private val partSet: Set<HTTagPrefix> = setOf(CommonTagPrefixes.GEAR, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)

    @JvmStatic
    private fun vanilla(event: HTMaterialPropertyEvent) {
        // Fuels
        event.modify(VanillaMaterialKeys.COAL) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addItemPrefixes(CommonTagPrefixes.DUST)
            addExtraOreResult(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR, 1 / 4f)
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.disableAll())

            setName("Coal", "石炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.disableAll())

            setName("Charcoal", "木炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            addExtraOreResult(CommonTagPrefixes.DUST, CommonMaterialKeys.CINNABAR, 1 / 4f)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(4))

            setName("Redstone", "赤石")
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Glowstone", "グロウストーン")
        }
        event.modify(VanillaMaterialKeys.CALCITE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Calcite", "方解石")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("white"))
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonTagPrefixes.DUST)
            addExtraOreResult(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER, 1 / 4f)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(4))

            setName("Lapis", "ラピス")
            setTextureSet("lapis")
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonTagPrefixes.DUST)
            addExtraOreResult(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR, 1 / 4f)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Quartz", "水晶")
            setTextureSet("quartz", HTMaterialTextureSet.SHINE)
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Amethyst", "アメジスト")
            setTextureSet("amethyst")
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR)
            addToolPrefixes(VanillaEquipmentMaterial.DIAMOND, CommonToolTypes.HAMMER)
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 1 / 4f)

            setName("Diamond", "ダイヤモンド")
            setTextureSet("diamond")
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR)
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.PRISMARINE, 1 / 4f)

            setName("Emerald", "エメラルド")
            setTextureSet("emerald")
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST)
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.ENDER, 1 / 4f)

            setName("Echo Shard", "残響の欠片")
            setTextureSet("echo")
        }
        event.modify(VanillaMaterialKeys.PRISMARINE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonTagPrefixes.DUST)
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ, 1 / 4f)

            setName("Prismarine", "プリズマリン")
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST)

            setName("Ender Pearl", "エンダーパール")
            setTextureSet("pearl")
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD, 1 / 4f)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3, 2))

            setName("Copper", "銅")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        event.modify(VanillaMaterialKeys.IRON) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )
            addToolPrefixes(VanillaEquipmentMaterial.IRON, CommonToolTypes.HAMMER)
            addExtraOreResult(CommonTagPrefixes.DUST, CommonMaterialKeys.TIN, 1 / 4f)

            setName("Iron", "鉄")
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )
            addToolPrefixes(VanillaEquipmentMaterial.GOLD, CommonToolTypes.HAMMER)
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER, 1 / 4f)

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
            addToolPrefixes(VanillaEquipmentMaterial.NETHERITE, CommonToolTypes.HAMMER)

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
        // Crops
        event.modify(VanillaMaterialKeys.WHEAT) {
            setDefaultPart(HTDefaultPart.Prefixed.CROP)
            addItemPrefixes(CommonTagPrefixes.FLOUR, CommonTagPrefixes.DOUGH)
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.CRUSHED_PREFIX, CommonTagPrefixes.FLOUR)

            setName("Wheat", "小麦")
            setTextureSet("crop")
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            setDefaultPart(ItemTags.PLANKS, HTItemHolderLike.of(Items.OAK_PLANKS))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR, CommonTagPrefixes.PLATE)
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.pressOnly())

            setName("Wood", "木")
            addCustomName(CommonTagPrefixes.DUST, "Sawdust", "おがくず")
            setTextureSet("wood")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
        }
        event.modify(VanillaMaterialKeys.GLASS) {
            setDefaultPart(Tags.Items.GLASS_BLOCKS, HTItemHolderLike.of(Items.GLASS))
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.ROD)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.solidifyOnly())
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_GLASS))

            setName("Glass", "ガラス")
            setTextureSet(HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("white"))
        }
        event.modify(VanillaMaterialKeys.STONE) {
            setDefaultPart(ItemTags.STONE_CRAFTING_MATERIALS, HTItemHolderLike.of(Items.COBBLESTONE))
            addToolPrefixes(VanillaEquipmentMaterial.STONE, CommonToolTypes.HAMMER)

            setName("Stone", "石")
            setTextureSet(HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            setDefaultPart(Tags.Items.OBSIDIANS_NORMAL, HTItemHolderLike.of(Items.OBSIDIAN))
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)

            setName("Obsidian", "黒曜石")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        event.modify(VanillaMaterialKeys.GUNPOWDER) {
            setDefaultPart(Tags.Items.GUNPOWDERS, HTItemHolderLike.of(Items.GUNPOWDER))
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)

            setName("Gunpowder", "火薬")
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

        fun registerMetal(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                setDefaultPart(HTDefaultPart.Prefixed.INGOT)

                setName(enName, jaName)
            }
        }

        fun registerMineral(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                setDefaultPart(HTDefaultPart.Prefixed.DUST)
                addBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK))
                addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.RAW)
                put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
                put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

                setName(enName, jaName)
                setTextureSet("mineral", HTMaterialTextureSet.DULL)
                addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
            }
        }
        // Fuels
        event.modify(CommonMaterialKeys.COAL_COKE) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL)
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.disableAll())

            setName("Coal Coke", "石炭コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        // Minerals
        registerMineral(CommonMaterialKeys.BAUXITE, "Bauxite", "ボーキサイト")
        registerMineral(CommonMaterialKeys.SALT, "Salt", "塩")
        registerMineral(CommonMaterialKeys.SALTPETER, "Saltpeter", "硝石")
        registerMineral(CommonMaterialKeys.SULFUR, "Sulfur", "硫黄")
        // Gems
        event.modify(CommonMaterialKeys.CINNABAR) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(materialBlockSet)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.RAW, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

            setName("Cinnabar", "辰砂")
            setTextureSet("lapis")
            addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
        }

        event.modify(CommonMaterialKeys.FLUORITE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

            setName("Fluorite", "蛍石")
        }
        registerGem(CommonMaterialKeys.PERIDOT, "Peridot", "ペリドット")
        registerGem(CommonMaterialKeys.RUBY, "Ruby", "ルビー")
        registerGem(CommonMaterialKeys.SAPPHIRE, "Sapphire", "サファイア")
        // Metals
        registerMetal(CommonMaterialKeys.LITHIUM, "Lithium", "リチウム")
        registerMetal(CommonMaterialKeys.BERYLLIUM, "Beryllium", "ベリリウム")

        registerMetal(CommonMaterialKeys.SODIUM, "Sodium", "ナトリウム")
        registerMetal(CommonMaterialKeys.MAGNESIUM, "Magnesium", "マグネシウム")

        event.modify(CommonMaterialKeys.ALUMINUM) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet)

            setName("Aluminum", "アルミニウム")
        }
        registerMetal(CommonMaterialKeys.SILICON, "Silicon", "シリコン")

        registerMetal(CommonMaterialKeys.TITANIUM, "Titanium", "チタン")
        registerMetal(CommonMaterialKeys.VANADIUM, "Vanadium", "バナジウム")
        registerMetal(CommonMaterialKeys.CHROMIUM, "Chromium", "クロム")
        registerMetal(CommonMaterialKeys.MANGANESE, "Manganese", "マンガン")
        registerMetal(CommonMaterialKeys.COBALT, "Cobalt", "コバルト")
        registerMetal(CommonMaterialKeys.NICKEL, "Nickel", "ニッケル")
        event.modify(CommonMaterialKeys.ZINC) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addItemPrefixes(metalSet)

            setName("Zinc", "亜鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
            addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
        }

        registerMetal(CommonMaterialKeys.MOLYBDENUM, "Molybdenum", "モリブデン")
        registerMetal(CommonMaterialKeys.PALLADIUM, "Palladium", "パラジウム")
        registerMetal(CommonMaterialKeys.SILVER, "Silver", "銀")
        event.modify(CommonMaterialKeys.TIN) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addItemPrefixes(metalSet)
            addExtraOreResult(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON, 1 / 4f)

            setName("Tin", "錫")
            setTextureSet(HTMaterialTextureSet.DULL)
            addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("white"))
        }
        registerMetal(CommonMaterialKeys.ANTIMONY, "Antimony", "アンチモン")

        registerMetal(CommonMaterialKeys.TUNGSTEN, "Tungsten", "パラジウム")
        registerMetal(CommonMaterialKeys.OSMIUM, "Osmium", "オスミウム")
        event.modify(CommonMaterialKeys.IRIDIUM) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(metalSet.plus(partSet))
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)

            setName("Iridium", "イリジウム")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        registerMetal(CommonMaterialKeys.PLATINUM, "Platinum", "白金")
        event.modify(CommonMaterialKeys.LEAD) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addItemPrefixes(metalSet)
            addExtraOreResult(CommonTagPrefixes.DUST, CommonMaterialKeys.SILVER, 1 / 4f)

            setName("Lead", "鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
            addCustomOreLoot(HTBlockLootFactory.createOre(CommonTagPrefixes.RAW))
        }

        registerMetal(CommonMaterialKeys.URANIUM, "Uranium", "ウラン")
        registerMetal(CommonMaterialKeys.PLUTONIUM, "Plutonium", "プルトニウム")
        // Alloys
        event.modify(CommonMaterialKeys.STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet).plus(CommonTagPrefixes.WIRE))
            addToolPrefixes(HCToolMaterials.STEEL, CommonToolTypes.VANILLA_SET.plus(CommonToolTypes.HAMMER))

            setName("Steel", "鋼鉄")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }

        registerMetal(CommonMaterialKeys.INVAR, "Invar", "不変鋼")
        registerMetal(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        event.modify(CommonMaterialKeys.BRASS) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Brass", "真鍮")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        event.modify(CommonMaterialKeys.BRONZE) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.BRONZE, CommonToolTypes.VANILLA_SET.plus(CommonToolTypes.HAMMER))

            setName("Bronze", "青銅")
        }
        registerMetal(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        registerMetal(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        registerMetal(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        registerMetal(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
        // Others
        event.modify(CommonMaterialKeys.ASH) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)

            setName("Ash", "灰")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        event.modify(CommonMaterialKeys.CARBON) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.pressOnly())

            setName("Carbon", "炭素")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("coal"))
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.INGOT,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.solidifyOnly())
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_PLASTIC))

            setName("Plastic", "プラスチック")
            addCustomName(CommonTagPrefixes.DUST, "Plastic Pulp", "プラスチックパルプ")
            addCustomName(CommonTagPrefixes.INGOT, "Plastic Bar", "プラスチックバー")
            addCustomName(CommonTagPrefixes.PLATE, "Plastic Sheet", "プラスチックシート")
            addCustomName(CommonTagPrefixes.WIRE, "Synthetic Fiber", "合成繊維")
            setTextureSet("polymer")
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.INGOT,
                CommonTagPrefixes.PLATE,
            )
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)
            put(HTMaterialPropertyKeys.FORMING_RECIPE_FLAG, HTFormingRecipeFlag.solidifyOnly())
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_RUBBER))

            setName("Rubber", "ゴム")
            addCustomName(CommonTagPrefixes.DUST, "Rubber Pulp", "ゴムパルプ")
            addCustomName(CommonTagPrefixes.INGOT, "Rubber Bar", "ゴムバー")
            addCustomName(CommonTagPrefixes.PLATE, "Rubber Sheet", "ゴムシート")
            setTextureSet("polymer")
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
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_CRIMSON_CRYSTAL))

            setName("Crimson Crystal", "深紅のクリスタリル")
            setTextureSet("emerald")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 24)
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_WARPED_CRYSTAL))

            setName("Warped Crystal", "歪んだクリスタリル")
            setTextureSet("emerald")
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PEARL)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_ELDRITCH))

            setName("Eldritch Pearl", "異質な真珠")
            setTextureSet("pearl")
        }
        // Metals
        event.modify(HCMaterialKeys.ANCIENT_METAL) {
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
            addToolPrefixes(HCToolMaterials.ANCIENT_METAL, CommonToolTypes.VANILLA_SET.plus(CommonToolTypes.HAMMER))
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)

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
            put(HTMaterialPropertyKeys.CAN_BE_SMELTED, false)

            setName("Ominous Metal", "不吉な金属")
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.AZURE_STEEL, CommonToolTypes.VANILLA_SET.plus(CommonToolTypes.HAMMER))

            setName("Azure Steel", "紺鉄")
        }
    }
}
