package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addColor
import hiiragi283.core.api.material.property.addDefaultPart
import hiiragi283.core.api.material.property.addName
import hiiragi283.core.api.material.property.addTemplate
import hiiragi283.core.common.data.texture.HCMaterialPalette
import hiiragi283.core.common.data.texture.HCTextureTemplates
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCMaterialEventHandler {
    @SubscribeEvent
    fun gatherAttributes(event: HTMaterialPropertyEvent) {
        vanilla(event)
        common(event)
        hiiragiCore(event)
    }

    private val smeltingToAsh: HTSmeltingMaterialProperty =
        HTSmeltingMaterialProperty.smeltingOnly(HCMaterialPrefixes.DUST, CommonMaterialKeys.ASH)

    @JvmStatic
    private fun vanilla(event: HTMaterialPropertyEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Fuels
        event.modify(VanillaMaterialKeys.COAL) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Coal", "石炭")
                addColor(HCMaterialPalette.COAL)
                addTemplate(HCTextureTemplates.FUEL)
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Charcoal", "木炭")
                addColor(HCMaterialPalette.CHARCOAL)
                addTemplate(HCTextureTemplates.FUEL)
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Redstone", "赤石")
                addColor(HCMaterialPalette.REDSTONE)
                addTemplate(HCTextureTemplates.DUST_SHINE)
            }
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Glowstone", "グロウストーン")
                addColor(HCMaterialPalette.GLOWSTONE)
                addTemplate(HCTextureTemplates.DUST_SHINE)
            }
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Lapis", "ラピス")
                addColor(HCMaterialPalette.LAPIS)
                addTemplate(HCTextureTemplates.GEM_LAPIS)
            }
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Quartz", "水晶")
                addColor(HCMaterialPalette.QUARTZ)
                addTemplate(HCTextureTemplates.GEM_QUARTZ)
            }
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Amethyst", "アメジスト")
                addColor(HCMaterialPalette.AMETHYST)
                addTemplate(HCTextureTemplates.GEM_AMETHYST)
            }
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Diamond", "ダイヤモンド")
                addColor(HCMaterialPalette.DIAMOND)
                addTemplate(HCTextureTemplates.GEM_DIAMOND)
            }
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Emerald", "エメラルド")
                addColor(HCMaterialPalette.EMERALD)
                addTemplate(HCTextureTemplates.GEM_EMERALD)
            }
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Echo Shard", "残響の欠片")
                addColor(HCMaterialPalette.ECHO)
                addTemplate(HCTextureTemplates.GEM_ECHO)
            }
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            addDefaultPart(HCMaterialPrefixes.PEARL)
            if (isDataGen) {
                addName("Ender Pearl", "エンダーパール")
                addColor(HCMaterialPalette.ENDER)
                addTemplate(HCTextureTemplates.PEARL)
            }
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Copper", "銅")
                addColor(HCMaterialPalette.COPPER)
                addTemplate(HCTextureTemplates.METAL)
            }
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Iron", "鉄")
                addColor(HCMaterialPalette.IRON)
                addTemplate(HCTextureTemplates.METAL_SHINE)
            }
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Gold", "金")
                addColor(HCMaterialPalette.GOLD)
                addTemplate(HCTextureTemplates.METAL_SHINE)
            }
        }
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Netherite", "ネザライト")
                addColor(HCMaterialPalette.NETHERITE)
                addTemplate(HCTextureTemplates.METAL_SHINE)
            }
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            if (isDataGen) {
                addName("Wood", "木")
                addColor(HCMaterialPalette.WOOD)
                addTemplate {
                    add(HCMaterialPrefixes.DUST)
                    addCustom(HCMaterialPrefixes.PLATE, "plate_wooden")
                }
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(VanillaMaterialKeys.STONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Stone", "石")
                addColor(HCMaterialPalette.STONE)
                addTemplate(HCTextureTemplates.DUST)
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            }
        }
        event.modify(VanillaMaterialKeys.GLASS) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Glass", "ガラス")
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            }
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Obsidian", "黒曜石")
                addColor(HCMaterialPalette.OBSIDIAN)
                addTemplate(HCTextureTemplates.DUST)
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            }
        }
    }

    @JvmStatic
    private fun common(event: HTMaterialPropertyEvent) {
        val isDataGen: Boolean = event.isDataGen

        fun register(
            key: HTMaterialKey,
            prefix: HTMaterialPrefix,
            enName: String,
            jaName: String,
        ) {
            event.modify(key) {
                addDefaultPart(prefix)
                if (prefix == HCMaterialPrefixes.INGOT) {
                }
                if (isDataGen) {
                    addName(enName, jaName)
                }
            }
        }
        // Fuels
        event.modify(CommonMaterialKeys.COAL_COKE) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Coal Coke", "石炭コークス")
                addColor(HCMaterialPalette.COAL_COKE)
                addTemplate(HCTextureTemplates.FUEL)
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(CommonMaterialKeys.CARBIDE) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Carbide", "カーバイド")
                addColor(HCMaterialPalette.CARBIDE)
                addTemplate(HCTextureTemplates.FUEL)
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        // Minerals
        event.modify(CommonMaterialKeys.CINNABAR) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Cinnabar", "辰砂")
                addColor(HCMaterialPalette.CINNABAR)
                addTemplate(HCTextureTemplates.DUST)
            }
        }
        event.modify(CommonMaterialKeys.SALT) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Salt", "塩")
                addColor(HCMaterialPalette.SALT)
                addTemplate(HCTextureTemplates.DUST_SHINE)
            }
        }
        event.modify(CommonMaterialKeys.SALTPETER) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Saltpeter", "硝石")
                addColor(HCMaterialPalette.SALTPETER)
                addTemplate(HCTextureTemplates.DUST)
            }
        }
        event.modify(CommonMaterialKeys.SULFUR) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Sulfur", "硫黄")
                addColor(HCMaterialPalette.SULFUR)
                addTemplate(HCTextureTemplates.DUST)
            }
        }
        // Gems
        register(CommonMaterialKeys.FLUORITE, HCMaterialPrefixes.GEM, "Fluorite", "蛍石")
        register(CommonMaterialKeys.PERIDOT, HCMaterialPrefixes.GEM, "Peridot", "ペリドット")
        register(CommonMaterialKeys.RUBY, HCMaterialPrefixes.GEM, "Ruby", "ルビー")
        register(CommonMaterialKeys.SAPPHIRE, HCMaterialPrefixes.GEM, "Sapphire", "サファイア")
        // Metals
        register(CommonMaterialKeys.ALUMINUM, HCMaterialPrefixes.INGOT, "Aluminum", "アルミニウム")

        register(CommonMaterialKeys.TITANIUM, HCMaterialPrefixes.INGOT, "Titanium", "チタン")
        register(CommonMaterialKeys.CHROME, HCMaterialPrefixes.INGOT, "Chrome", "クロム")
        register(CommonMaterialKeys.CHROMIUM, HCMaterialPrefixes.INGOT, "Chromium", "クロム")
        register(CommonMaterialKeys.MANGANESE, HCMaterialPrefixes.INGOT, "Manganese", "マンガン")
        register(CommonMaterialKeys.COBALT, HCMaterialPrefixes.INGOT, "Cobalt", "コバルト")
        register(CommonMaterialKeys.NICKEL, HCMaterialPrefixes.INGOT, "Nickel", "ニッケル")
        register(CommonMaterialKeys.ZINC, HCMaterialPrefixes.INGOT, "Zinc", "亜鉛")

        register(CommonMaterialKeys.PALLADIUM, HCMaterialPrefixes.INGOT, "Palladium", "パラジウム")
        register(CommonMaterialKeys.SILVER, HCMaterialPrefixes.INGOT, "Silver", "銀")
        register(CommonMaterialKeys.TIN, HCMaterialPrefixes.INGOT, "Tin", "錫")
        register(CommonMaterialKeys.ANTIMONY, HCMaterialPrefixes.INGOT, "Antimony", "アンチモン")

        register(CommonMaterialKeys.TUNGSTEN, HCMaterialPrefixes.INGOT, "Tungsten", "パラジウム")
        register(CommonMaterialKeys.OSMIUM, HCMaterialPrefixes.INGOT, "Osmium", "オスミウム")
        register(CommonMaterialKeys.IRIDIUM, HCMaterialPrefixes.INGOT, "Iridium", "イリジウム")
        register(CommonMaterialKeys.PLATINUM, HCMaterialPrefixes.INGOT, "Platinum", "白金")
        register(CommonMaterialKeys.LEAD, HCMaterialPrefixes.INGOT, "Lead", "鉛")

        register(CommonMaterialKeys.URANIUM, HCMaterialPrefixes.INGOT, "Uranium", "ウラン")
        // Alloys
        event.modify(CommonMaterialKeys.STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Steel", "鋼鉄")
                addColor(HCMaterialPalette.STEEL)
                addTemplate(HCTextureTemplates.METAL_SHINE)
            }
        }
        register(CommonMaterialKeys.INVAR, HCMaterialPrefixes.INGOT, "Invar", "不変鋼")
        register(CommonMaterialKeys.CONSTANTAN, HCMaterialPrefixes.INGOT, "Constantan", "コンスタンタン")
        register(CommonMaterialKeys.BRASS, HCMaterialPrefixes.INGOT, "Brass", "真鍮")
        register(CommonMaterialKeys.BRONZE, HCMaterialPrefixes.INGOT, "Bronze", "青銅")
        register(CommonMaterialKeys.ELECTRUM, HCMaterialPrefixes.INGOT, "Electrum", "琥珀金")

        register(CommonMaterialKeys.SIGNALUM, HCMaterialPrefixes.INGOT, "Signalum", "シグナルム")
        register(CommonMaterialKeys.LUMIUM, HCMaterialPrefixes.INGOT, "Lumium", "ルミウム")
        register(CommonMaterialKeys.ENDERIUM, HCMaterialPrefixes.INGOT, "Enderium", "エンダリウム")
        // Others
        event.modify(CommonMaterialKeys.ASH) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Ash", "灰")
                addColor(HCMaterialPalette.STEEL)
                addTemplate(HCTextureTemplates.DUST_DULL)
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            }
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            addDefaultPart(HCMaterialPrefixes.PLATE)
            if (isDataGen) {
                addName("Plastic", "プラスチック")
                addColor(HCMaterialPalette.PLASTIC)
                addTemplate {
                    addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_plate")
                    add(HCMaterialPrefixes.RAW_MATERIAL)
                    add(HCMaterialPrefixes.PLATE)
                }
            }
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            addDefaultPart(HCMaterialPrefixes.PLATE)
            if (isDataGen) {
                addName("Rubber", "ゴム")
                addColor(HCMaterialPalette.RUBBER)
                addTemplate {
                    addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_plate")
                    add(HCMaterialPrefixes.PLATE)
                }
            }
        }
    }

    @JvmStatic
    private fun hiiragiCore(event: HTMaterialPropertyEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Gems
        event.modify(HCMaterialKeys.AZURE) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Azure Shard", "紺碧の欠片")
                addColor(HCMaterialPalette.AZURE_STEEL)
                addTemplate(HCTextureTemplates.GEM_AMETHYST)
            }
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_CRIMSON_CRYSTAL))
            if (isDataGen) {
                addName("Crimson Crystal", "深紅のクリスタリル")
                addColor(HCMaterialPalette.CRIMSON_CRYSTAL)
                addTemplate(HCTextureTemplates.GEM_EMERALD)
            }
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_WARPED_CRYSTAL))
            if (isDataGen) {
                addName("Warped Crystal", "歪んだクリスタリル")
                addColor(HCMaterialPalette.WARPED_CRYSTAL)
                addTemplate(HCTextureTemplates.GEM_EMERALD)
            }
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            addDefaultPart(HCMaterialPrefixes.PEARL)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_ELDRITCH))
            if (isDataGen) {
                addName("Eldritch Pearl", "異質な真珠")
                addColor(HCMaterialPalette.ELDRITCH)
                addTemplate(HCTextureTemplates.PEARL)
            }
        }
        // Metals
        event.modify(HCMaterialKeys.NIGHT_METAL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Night Metal", "夜金")
                addColor(HCMaterialPalette.NIGHT_METAL)
                addTemplate(HCTextureTemplates.METAL_SHINE)
            }
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Azure Steel", "紺鉄")
                addColor(HCMaterialPalette.AZURE_STEEL)
                addTemplate(HCTextureTemplates.METAL)
            }
        }
        event.modify(HCMaterialKeys.DEEP_STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Deep Steel", "深層鋼")
                addColor(HCMaterialPalette.DEEP_STEEL)
                addTemplate(HCTextureTemplates.METAL)
            }
        }
    }
}
