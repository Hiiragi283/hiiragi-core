package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addDefaultPart
import hiiragi283.core.api.material.property.addName
import hiiragi283.core.api.material.property.addTextureSet
import hiiragi283.core.api.material.property.setBlockPrefixes
import hiiragi283.core.api.material.property.setItemPrefixes
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
            addDefaultPart(CommonTagPrefixes.FUEL)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Coal", "石炭")
            addTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addDefaultPart(CommonTagPrefixes.FUEL)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Charcoal", "木炭")
            addTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 8)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
            addDefaultPart(CommonTagPrefixes.DUST)

            addName("Redstone", "赤石")
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addDefaultPart(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            addName("Glowstone", "グロウストーン")
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Lapis", "ラピス")
            addTextureSet("lapis")
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            addName("Quartz", "水晶")
            addTextureSet("quartz")
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            addName("Amethyst", "アメジスト")
            addTextureSet("amethyst")
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Diamond", "ダイヤモンド")
            addTextureSet("diamond")
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Emerald", "エメラルド")
            addTextureSet("emerald")
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Echo Shard", "残響の欠片")
            addTextureSet("echo")
        }
        event.modify(VanillaMaterialKeys.PRISMARINE) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Prismarine", "プリズマリン")
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            addDefaultPart(Tags.Items.ENDER_PEARLS, HTItemHolderLike.Simple(Items.ENDER_PEARL))
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Ender Pearl", "エンダーパール")
            addTextureSet("pearl")
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )

            addName("Copper", "銅")
            addTextureSet("shine")
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )

            addName("Iron", "鉄")
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )

            addName("Gold", "金")
        }
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.GEAR,
                CommonTagPrefixes.NUGGET,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
            )

            addName("Netherite", "ネザライト")
            addTextureSet("dull")
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            addDefaultPart(ItemTags.PLANKS, HTItemHolderLike.Simple(Items.OAK_PLANKS))
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEAR, CommonTagPrefixes.PLATE)

            addName("Wood", "木")
            addCustomName(CommonTagPrefixes.DUST, "Sawdust", "おがくず")
            addTextureSet("wood")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 15)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        event.modify(VanillaMaterialKeys.GLASS) {
            addDefaultPart(Tags.Items.GLASS_BLOCKS, HTItemHolderLike.Simple(Items.GLASS))
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.ROD)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_GLASS))

            addName("Glass", "ガラス")
            addTextureSet("shine")
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.smeltingOnly(HTItemHolderLike.Simple(Items.GLASS)))
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("salt"))
        }
        event.modify(VanillaMaterialKeys.STONE) {
            addName("Stone", "石")
            addTextureSet("dull")
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            addDefaultPart(Tags.Items.OBSIDIANS_NORMAL, HTItemHolderLike.Simple(Items.OBSIDIAN))
            setItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)

            addName("Obsidian", "黒曜石")
            addTextureSet("dull")
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
        }
    }

    @JvmStatic
    private fun common(event: HTMaterialPropertyEvent) {
        fun registerGem(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                addDefaultPart(CommonTagPrefixes.GEM)

                addName(enName, jaName)
            }
        }

        fun registerMetal(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                addDefaultPart(CommonTagPrefixes.INGOT)

                addName(enName, jaName)
            }
        }
        // Fuels
        event.modify(CommonMaterialKeys.COAL_COKE) {
            addDefaultPart(
                HiiragiCoreTags.Items.COAL_COKE,
                HTDeferredItem.simple(CommonTagPrefixes.FUEL.createId(CommonMaterialKeys.COAL_COKE)),
            )
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL)

            addName("Coal Coke", "石炭コークス")
            addTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        // Minerals
        event.modify(CommonMaterialKeys.BAUXITE) {
            addDefaultPart(CommonTagPrefixes.DUST)
            setBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.BLOCK))
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Bauxite", "ボーキサイト")
            addTextureSet("mineral", HTMaterialTextureSet.DULL)
        }
        event.modify(CommonMaterialKeys.CINNABAR) {
            addDefaultPart(CommonTagPrefixes.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Cinnabar", "辰砂")
            addTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("crimson_crystal"))
        }
        event.modify(CommonMaterialKeys.SALT) {
            addDefaultPart(CommonTagPrefixes.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Salt", "塩")
            addTextureSet("mineral")
        }
        event.modify(CommonMaterialKeys.SALTPETER) {
            addDefaultPart(CommonTagPrefixes.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Saltpeter", "硝石")
            addTextureSet("mineral")
        }
        event.modify(CommonMaterialKeys.SULFUR) {
            addDefaultPart(CommonTagPrefixes.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Sulfur", "硫黄")
            addTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("gold"))
        }
        // Gems
        registerGem(CommonMaterialKeys.FLUORITE, "Fluorite", "蛍石")
        registerGem(CommonMaterialKeys.PERIDOT, "Peridot", "ペリドット")
        registerGem(CommonMaterialKeys.RUBY, "Ruby", "ルビー")
        registerGem(CommonMaterialKeys.SAPPHIRE, "Sapphire", "サファイア")
        // Metals
        event.modify(CommonMaterialKeys.ALUMINUM) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet)

            addName("Aluminum", "アルミニウム")
            addTextureSet(HTMaterialTextureSet.SHINE)
        }

        registerMetal(CommonMaterialKeys.TITANIUM, "Titanium", "チタン")
        registerMetal(CommonMaterialKeys.CHROME, "Chrome", "クロム")
        registerMetal(CommonMaterialKeys.CHROMIUM, "Chromium", "クロム")
        registerMetal(CommonMaterialKeys.MANGANESE, "Manganese", "マンガン")
        registerMetal(CommonMaterialKeys.COBALT, "Cobalt", "コバルト")
        registerMetal(CommonMaterialKeys.NICKEL, "Nickel", "ニッケル")
        event.modify(CommonMaterialKeys.ZINC) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK).plus(CommonTagPrefixes.BLOCK))
            setItemPrefixes(metalSet)

            addName("Zinc", "亜鉛")
            addTextureSet(HTMaterialTextureSet.DULL)
        }

        registerMetal(CommonMaterialKeys.PALLADIUM, "Palladium", "パラジウム")
        registerMetal(CommonMaterialKeys.SILVER, "Silver", "銀")
        event.modify(CommonMaterialKeys.TIN) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK).plus(CommonTagPrefixes.BLOCK))
            setItemPrefixes(metalSet)

            addName("Tin", "錫")
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
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet).plus(CommonTagPrefixes.WIRE))

            addName("Steel", "鋼鉄")
            addTextureSet(HTMaterialTextureSet.SHINE)
        }

        registerMetal(CommonMaterialKeys.INVAR, "Invar", "不変鋼")
        registerMetal(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        event.modify(CommonMaterialKeys.BRASS) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet))

            addName("Brass", "真鍮")
            addTextureSet(HTMaterialTextureSet.DULL)
        }
        event.modify(CommonMaterialKeys.BRONZE) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet))

            addName("Bronze", "青銅")
        }
        registerMetal(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        registerMetal(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        registerMetal(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        registerMetal(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
        // Others
        event.modify(CommonMaterialKeys.ASH) {
            addDefaultPart(CommonTagPrefixes.DUST)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST)

            addName("Ash", "灰")
            addTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            addDefaultPart(CommonTagPrefixes.PLATE)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(
                CommonTagPrefixes.DUST,
                CommonTagPrefixes.PLATE,
                CommonTagPrefixes.ROD,
                CommonTagPrefixes.WIRE,
            )
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_PLASTIC))

            addName("Plastic", "プラスチック")
            addTextureSet("plate", HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            addDefaultPart(CommonTagPrefixes.PLATE)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.PLATE)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_RUBBER))

            addName("Rubber", "ゴム")
            addCustomName(CommonTagPrefixes.PLATE, "Rubber Sheet", "ゴムシート")
            addTextureSet("plate")
            put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
        }
    }

    @JvmStatic
    private fun hiiragiCore(event: HTMaterialPropertyEvent) {
        // Gems
        event.modify(HCMaterialKeys.AZURE) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            addName("Azure Shard", "紺碧の欠片")
            addTextureSet("amethyst", HTMaterialTextureSet.SHINE)
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_CRIMSON_CRYSTAL))

            addName("Crimson Crystal", "深紅のクリスタリル")
            addTextureSet("emerald")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 24)
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            addDefaultPart(CommonTagPrefixes.GEM)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_WARPED_CRYSTAL))

            addName("Warped Crystal", "歪んだクリスタリル")
            addTextureSet("emerald")
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            addDefaultPart(CommonTagPrefixes.PEARL)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PEARL)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_ELDRITCH))

            addName("Eldritch Pearl", "異質な真珠")
            addTextureSet("pearl")
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            addDefaultPart(CommonTagPrefixes.INGOT)
            setBlockPrefixes(CommonTagPrefixes.BLOCK)
            setItemPrefixes(alloySet.plus(partSet))

            addName("Azure Steel", "紺鉄")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("azure"))
        }
        event.modify(HCMaterialKeys.DEEP_STEEL) {
            addDefaultPart(CommonTagPrefixes.INGOT)
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

            addName("Deep Steel", "深層鋼")
        }
    }
}
