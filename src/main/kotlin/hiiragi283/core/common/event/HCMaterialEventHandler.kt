package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialDefinitionEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.addColor
import hiiragi283.core.api.material.addDefaultPrefix
import hiiragi283.core.api.material.addName
import hiiragi283.core.api.material.attribute.HTSmeltingMaterialAttribute
import hiiragi283.core.api.material.attribute.HTStorageBlockMaterialAttribute
import hiiragi283.core.api.material.attribute.HTTextureTemplateMaterialAttribute
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.data.texture.HCMaterialPalette
import hiiragi283.core.common.data.texture.HCTextureTemplates
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HCMaterialEventHandler {
    @SubscribeEvent
    fun gatherAttributes(event: HTMaterialDefinitionEvent) {
        vanilla(event)
        common(event)
        hiiragiCore(event)
    }

    @JvmStatic
    private fun vanilla(event: HTMaterialDefinitionEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Fuels
        event.modify(VanillaMaterialKeys.COAL) {
            addDefaultPrefix(HCMaterialPrefixes.FUEL)
            add(HTSmeltingMaterialAttribute.disable())
            if (isDataGen) {
                addName("Coal", "石炭")
                addColor(HCMaterialPalette.COAL)
                add(HCTextureTemplates.FUEL)
            }
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addDefaultPrefix(HCMaterialPrefixes.FUEL)
            add(HTSmeltingMaterialAttribute.disable())
            if (isDataGen) {
                addName("Charcoal", "木炭")
                addColor(HCMaterialPalette.CHARCOAL)
                add(HCTextureTemplates.FUEL)
            }
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Redstone", "赤石")
                addColor(HCMaterialPalette.REDSTONE)
                add(HCTextureTemplates.DUST_SHINE)
            }
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Glowstone", "グロウストーン")
                addColor(HCMaterialPalette.GLOWSTONE)
                add(HCTextureTemplates.DUST_SHINE)
            }
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Lapis", "ラピス")
                addColor(HCMaterialPalette.LAPIS)
                add(HCTextureTemplates.GEM_LAPIS)
            }
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            add(HTStorageBlockMaterialAttribute.TWO_BY_TWO)
            if (isDataGen) {
                addName("Quartz", "水晶")
                addColor(HCMaterialPalette.QUARTZ)
                add(HCTextureTemplates.GEM_QUARTZ)
            }
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            add(HTStorageBlockMaterialAttribute.TWO_BY_TWO)
            if (isDataGen) {
                addName("Amethyst", "アメジスト")
                addColor(HCMaterialPalette.AMETHYST)
                add(HCTextureTemplates.GEM_AMETHYST)
            }
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Diamond", "ダイヤモンド")
                addColor(HCMaterialPalette.DIAMOND)
                add(HCTextureTemplates.GEM_DIAMOND)
            }
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Emerald", "エメラルド")
                addColor(HCMaterialPalette.EMERALD)
                add(HCTextureTemplates.GEM_EMERALD)
            }
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Echo Shard", "残響の欠片")
                addColor(HCMaterialPalette.ECHO)
                add(HCTextureTemplates.GEM_ECHO)
            }
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            addDefaultPrefix(HCMaterialPrefixes.PEARL)
            if (isDataGen) {
                addName("Ender Pearl", "エンダーパール")
                addColor(HCMaterialPalette.ENDER)
                add(HCTextureTemplates.PEARL)
            }
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Copper", "銅")
                addColor(HCMaterialPalette.COPPER)
                add(HCTextureTemplates.METAL)
            }
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Iron", "鉄")
                addColor(HCMaterialPalette.IRON)
                add(HCTextureTemplates.METAL)
            }
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Gold", "金")
                addColor(HCMaterialPalette.GOLD)
                add(HCTextureTemplates.METAL_SHINE)
            }
        }
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Netherite", "ネザライト")
                addColor(HCMaterialPalette.NETHERITE)
                add(HCTextureTemplates.METAL_SHINE)
            }
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            add(HTSmeltingMaterialAttribute.disable())
            if (isDataGen) {
                addName("Wood", "木")
                addColor(HCMaterialPalette.WOOD)
                add(
                    HTTextureTemplateMaterialAttribute.create {
                        add(HCMaterialPrefixes.DUST)
                        addCustom(HCMaterialPrefixes.PLATE, "plate_wooden")
                    },
                )
            }
        }
        event.modify(VanillaMaterialKeys.STONE) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Stone", "石")
            }
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Obsidian", "黒曜石")
                addColor(HCMaterialPalette.OBSIDIAN)
                add(HCTextureTemplates.DUST)
            }
        }
    }

    @JvmStatic
    private fun common(event: HTMaterialDefinitionEvent) {
        val isDataGen: Boolean = event.isDataGen

        fun register(
            key: HTMaterialKey,
            prefix: HTMaterialPrefix,
            enName: String,
            jaName: String,
        ) {
            event.modify(key) {
                addDefaultPrefix(prefix)
                if (isDataGen) {
                    addName(enName, jaName)
                }
            }
        }
        // Fuels
        event.modify(CommonMaterialKeys.COAL_COKE) {
            addDefaultPrefix(HCMaterialPrefixes.FUEL)
            add(HTSmeltingMaterialAttribute.disable())
            if (isDataGen) {
                addName("Coal Coke", "石炭コークス")
                addColor(HCMaterialPalette.COAL_COKE)
                add(HCTextureTemplates.FUEL)
            }
        }
        event.modify(CommonMaterialKeys.CARBIDE) {
            addDefaultPrefix(HCMaterialPrefixes.FUEL)
            add(HTSmeltingMaterialAttribute.disable())
            if (isDataGen) {
                addName("Carbide", "カーバイド")
                addColor(HCMaterialPalette.CARBIDE)
                add(HCTextureTemplates.FUEL)
            }
        }
        // Minerals
        event.modify(CommonMaterialKeys.CINNABAR) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Cinnabar", "辰砂")
                addColor(HCMaterialPalette.CINNABAR)
                add(HCTextureTemplates.DUST)
            }
        }
        event.modify(CommonMaterialKeys.SALT) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Salt", "塩")
                addColor(HCMaterialPalette.SALT)
                add(HCTextureTemplates.DUST_SHINE)
            }
        }
        event.modify(CommonMaterialKeys.SALTPETER) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Saltpeter", "硝石")
                addColor(HCMaterialPalette.SALTPETER)
                add(HCTextureTemplates.DUST)
            }
        }
        event.modify(CommonMaterialKeys.SULFUR) {
            addDefaultPrefix(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Sulfur", "硫黄")
                addColor(HCMaterialPalette.SULFUR)
                add(HCTextureTemplates.DUST)
            }
        }
        // Gems
        register(CommonMaterialKeys.FLUORITE, HCMaterialPrefixes.GEM, "Fluorite", "蛍石")
        register(CommonMaterialKeys.PERIDOT, HCMaterialPrefixes.GEM, "Peridot", "ペリドット")
        register(CommonMaterialKeys.RUBY, HCMaterialPrefixes.GEM, "Ruby", "ルビー")
        register(CommonMaterialKeys.SAPPHIRE, HCMaterialPrefixes.GEM, "Sapphire", "サファイア")
        // Metals
        register(CommonMaterialKeys.ALUMINUM, HCMaterialPrefixes.GEM, "Aluminum", "アルミニウム")

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
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Steel", "鋼鉄")
                addColor(HCMaterialPalette.STEEL)
                add(HCTextureTemplates.METAL)
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
        // Plates
        event.modify(CommonMaterialKeys.PLASTIC) {
            addDefaultPrefix(HCMaterialPrefixes.PLATE)
            if (isDataGen) {
                addName("Plastic", "プラスチック")
                addColor(HCMaterialPalette.PLASTIC)
                add(
                    HTTextureTemplateMaterialAttribute.create {
                        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_plate")
                        add(HCMaterialPrefixes.RAW_MATERIAL)
                        add(HCMaterialPrefixes.PLATE)
                    },
                )
            }
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            addDefaultPrefix(HCMaterialPrefixes.PLATE)
            if (isDataGen) {
                addName("Rubber", "ゴム")
                addColor(HCMaterialPalette.RUBBER)
                add(
                    HTTextureTemplateMaterialAttribute.create {
                        addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_plate")
                        add(HCMaterialPrefixes.PLATE)
                    },
                )
            }
        }
    }

    @JvmStatic
    private fun hiiragiCore(event: HTMaterialDefinitionEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Gems
        event.modify(HCMaterialKeys.AZURE) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            add(HTStorageBlockMaterialAttribute.TWO_BY_TWO)
            if (isDataGen) {
                addName("Azure Shard", "紺碧の欠片")
                addColor(HCMaterialPalette.AZURE_STEEL)
                add(HCTextureTemplates.GEM_AMETHYST)
            }
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Crimson Crystal", "深紅のクリスタリル")
                addColor(HCMaterialPalette.CRIMSON_CRYSTAL)
                add(HCTextureTemplates.GEM_EMERALD)
            }
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            addDefaultPrefix(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Warped Crystal", "歪んだクリスタリル")
                addColor(HCMaterialPalette.WARPED_CRYSTAL)
                add(HCTextureTemplates.GEM_EMERALD)
            }
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            addDefaultPrefix(HCMaterialPrefixes.PEARL)
            if (isDataGen) {
                addName("Eldritch Pearl", "異質な真珠")
                addColor(HCMaterialPalette.ELDRITCH)
                add(HCTextureTemplates.PEARL)
            }
        }
        // Metals
        event.modify(HCMaterialKeys.NIGHT_METAL) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Night Metal", "夜金")
                addColor(HCMaterialPalette.NIGHT_METAL)
                add(HCTextureTemplates.METAL_SHINE)
            }
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Azure Steel", "紺鉄")
                addColor(HCMaterialPalette.AZURE_STEEL)
                add(HCTextureTemplates.METAL)
            }
        }
        event.modify(HCMaterialKeys.DEEP_STEEL) {
            addDefaultPrefix(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Deep Steel", "深層鋼")
                addColor(HCMaterialPalette.DEEP_STEEL)
                add(HCTextureTemplates.METAL)
            }
        }
    }
}
