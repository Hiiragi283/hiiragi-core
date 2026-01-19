package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.setBlockPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setItemPrefixes
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.setup.HCFluids
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

    private val smeltingToAsh: HTSmeltingMaterialProperty by lazy {
        HTSmeltingMaterialProperty.smeltingOnly(HTDeferredItem.simple(CommonTagPrefixes.DUST.createId(CommonMaterialKeys.ASH)))
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
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Coal", "石炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Charcoal", "木炭")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)

            setName("Redstone", "赤石")
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Glowstone", "グロウストーン")
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Lapis", "ラピス")
            setTextureSet("lapis")
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Quartz", "水晶")
            setTextureSet("quartz")
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Amethyst", "アメジスト")
            setTextureSet("amethyst")
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Diamond", "ダイヤモンド")
            setTextureSet("diamond")
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Emerald", "エメラルド")
            setTextureSet("emerald")
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Echo Shard", "残響の欠片")
            setTextureSet("echo")
        }
        event.modify(VanillaMaterialKeys.PRISMARINE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Prismarine", "プリズマリン")
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            setDefaultPart(Tags.Items.ENDER_PEARLS, HTItemHolderLike.Simple(Items.ENDER_PEARL))
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Ender Pearl", "エンダーパール")
            setTextureSet("pearl")
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )

            setName("Copper", "銅")
            setTextureSet("shine")
        }
        event.modify(VanillaMaterialKeys.IRON) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )

            setName("Iron", "鉄")
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )

            setName("Gold", "金")
        }
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )

            setName("Netherite", "ネザライト")
            setTextureSet("dull")
        }
        // Crops
        event.modify(VanillaMaterialKeys.WHEAT) {
            setDefaultPart(HTDefaultPart.Prefixed.CROP)
            setItemPrefixes(CommonTagPrefixes.FLOUR, CommonTagPrefixes.DOUGH)
            put(HTMaterialPropertyKeys.CRUSHED_PREFIX, CommonTagPrefixes.FLOUR)

            setName("Wheat", "小麦")
            setTextureSet("crop")
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            setDefaultPart(ItemTags.PLANKS, HTItemHolderLike.Simple(Items.OAK_PLANKS))
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR, CommonTagPrefixes.PLATE)

            setName("Wood", "木")
            addCustomName(CommonTagPrefixes.DUST, "Sawdust", "おがくず")
            setTextureSet("wood")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        event.modify(VanillaMaterialKeys.GLASS) {
            setDefaultPart(Tags.Items.GLASS_BLOCKS, HTItemHolderLike.Simple(Items.GLASS))
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.ROD)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_GLASS))

            setName("Glass", "ガラス")
            setTextureSet("shine")
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.smeltingOnly(HTItemHolderLike.Simple(Items.GLASS)))
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("salt"))
        }
        event.modify(VanillaMaterialKeys.STONE) {
            setName("Stone", "石")
            setTextureSet("dull")
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            setDefaultPart(Tags.Items.OBSIDIANS_NORMAL, HTItemHolderLike.Simple(Items.OBSIDIAN))
            setItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)

            setName("Obsidian", "黒曜石")
            setTextureSet("dull")
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
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
        // Fuels
        event.modify(CommonMaterialKeys.COAL_COKE) {
            setDefaultPart(
                HiiragiCoreTags.Items.COAL_COKE,
                HTDeferredItem.simple(CommonTagPrefixes.FUEL.createId(CommonMaterialKeys.COAL_COKE)),
            )
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL)

            setName("Coal Coke", "石炭コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        // Minerals
        event.modify(CommonMaterialKeys.BAUXITE) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            setBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.BLOCK))
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Bauxite", "ボーキサイト")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
        }
        event.modify(CommonMaterialKeys.CINNABAR) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Cinnabar", "辰砂")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("crimson_crystal"))
        }
        event.modify(CommonMaterialKeys.SALT) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Salt", "塩")
            setTextureSet("mineral")
        }
        event.modify(CommonMaterialKeys.SALTPETER) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Saltpeter", "硝石")
            setTextureSet("mineral")
        }
        event.modify(CommonMaterialKeys.SULFUR) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Sulfur", "硫黄")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("gold"))
        }
        // Gems
        registerGem(CommonMaterialKeys.FLUORITE, "Fluorite", "蛍石")
        registerGem(CommonMaterialKeys.PERIDOT, "Peridot", "ペリドット")
        registerGem(CommonMaterialKeys.RUBY, "Ruby", "ルビー")
        registerGem(CommonMaterialKeys.SAPPHIRE, "Sapphire", "サファイア")
        // Metals
        event.modify(CommonMaterialKeys.ALUMINUM) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet)

            setName("Aluminum", "アルミニウム")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }

        registerMetal(CommonMaterialKeys.TITANIUM, "Titanium", "チタン")
        registerMetal(CommonMaterialKeys.CHROME, "Chrome", "クロム")
        registerMetal(CommonMaterialKeys.CHROMIUM, "Chromium", "クロム")
        registerMetal(CommonMaterialKeys.MANGANESE, "Manganese", "マンガン")
        registerMetal(CommonMaterialKeys.COBALT, "Cobalt", "コバルト")
        registerMetal(CommonMaterialKeys.NICKEL, "Nickel", "ニッケル")
        event.modify(CommonMaterialKeys.ZINC) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK).plus(CommonTagPrefixes.BLOCK))
            setItemPrefixes(metalSet)

            setName("Zinc", "亜鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
        }

        registerMetal(CommonMaterialKeys.PALLADIUM, "Palladium", "パラジウム")
        registerMetal(CommonMaterialKeys.SILVER, "Silver", "銀")
        event.modify(CommonMaterialKeys.TIN) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK).plus(CommonTagPrefixes.BLOCK))
            setItemPrefixes(metalSet)

            setName("Tin", "錫")
        }
        registerMetal(CommonMaterialKeys.ANTIMONY, "Antimony", "アンチモン")

        registerMetal(CommonMaterialKeys.TUNGSTEN, "Tungsten", "パラジウム")
        registerMetal(CommonMaterialKeys.OSMIUM, "Osmium", "オスミウム")
        registerMetal(CommonMaterialKeys.IRIDIUM, "Iridium", "イリジウム")
        registerMetal(CommonMaterialKeys.PLATINUM, "Platinum", "白金")
        registerMetal(CommonMaterialKeys.LEAD, "Lead", "鉛")

        registerMetal(CommonMaterialKeys.URANIUM, "Uranium", "ウラン")
        registerMetal(CommonMaterialKeys.PLUTONIUM, "Plutonium", "プルトニウム")
        // Alloys
        event.modify(CommonMaterialKeys.STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet).plus(CommonTagPrefixes.WIRE))

            setName("Steel", "鋼鉄")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }

        registerMetal(CommonMaterialKeys.INVAR, "Invar", "不変鋼")
        registerMetal(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        event.modify(CommonMaterialKeys.BRASS) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet))

            setName("Brass", "真鍮")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        event.modify(CommonMaterialKeys.BRONZE) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet))

            setName("Bronze", "青銅")
        }
        registerMetal(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        registerMetal(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        registerMetal(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        registerMetal(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
        // Others
        event.modify(CommonMaterialKeys.ASH) {
            setDefaultPart(HTDefaultPart.Prefixed.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            setName("Ash", "灰")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            setDefaultPart(HTDefaultPart.Prefixed.PLATE)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_PLASTIC))

            setName("Plastic", "プラスチック")
            setTextureSet("plate", HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            setDefaultPart(HTDefaultPart.Prefixed.PLATE)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.PLATE)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_RUBBER))

            setName("Rubber", "ゴム")
            addCustomName(CommonTagPrefixes.PLATE, "Rubber Sheet", "ゴムシート")
            setTextureSet("plate")
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
    }

    @JvmStatic
    private fun hiiragiCore(event: HTMaterialPropertyEvent) {
        // Gems
        event.modify(HCMaterialKeys.AZURE) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Azure Shard", "紺碧の欠片")
            setTextureSet("amethyst", HTMaterialTextureSet.SHINE)
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_CRIMSON_CRYSTAL))

            setName("Crimson Crystal", "深紅のクリスタリル")
            setTextureSet("emerald")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 24)
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_WARPED_CRYSTAL))

            setName("Warped Crystal", "歪んだクリスタリル")
            setTextureSet("emerald")
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            setDefaultPart(HTDefaultPart.Prefixed.PEARL)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PEARL)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_ELDRITCH))

            setName("Eldritch Pearl", "異質な真珠")
            setTextureSet("pearl")
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet))

            setName("Azure Steel", "紺鉄")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("azure"))
        }
        event.modify(HCMaterialKeys.DEEP_STEEL) {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.SCRAP,
                CommonTagPrefixes.INGOT,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )

            setName("Deep Steel", "深層鋼")
        }
    }
}
