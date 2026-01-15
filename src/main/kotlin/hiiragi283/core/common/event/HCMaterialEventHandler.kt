package hiiragi283.core.common.event

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.HTTextureTemplate
import hiiragi283.core.api.material.property.addDefaultPart
import hiiragi283.core.api.material.property.addName
import hiiragi283.core.api.material.property.addTemplate
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
                addTemplate(HTTextureTemplate.default())
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Charcoal", "木炭")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_fuel"))
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        // Minerals
        event.modify(VanillaMaterialKeys.REDSTONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Redstone", "赤石")
            }
        }
        event.modify(VanillaMaterialKeys.GLOWSTONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Glowstone", "グロウストーン")
            }
        }
        // Gems
        event.modify(VanillaMaterialKeys.LAPIS) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Lapis", "ラピス")
                addTemplate(HTTextureTemplate.default())
            }
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Quartz", "水晶")
                addTemplate(HTTextureTemplate.default())
            }
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Amethyst", "アメジスト")
                addTemplate(HTTextureTemplate.default())
            }
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Diamond", "ダイヤモンド")
                addTemplate(HTTextureTemplate.default())
            }
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Emerald", "エメラルド")
                addTemplate(HTTextureTemplate.default())
            }
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Echo Shard", "残響の欠片")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_gem"))
            }
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            addDefaultPart(HCMaterialPrefixes.PEARL)
            if (isDataGen) {
                addName("Ender Pearl", "エンダーパール")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_pearl"))
            }
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Copper", "銅")
                addTemplate(HTTextureTemplate.shine())
            }
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Iron", "鉄")
                addTemplate(HTTextureTemplate.default())
            }
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Gold", "金")
                addTemplate(HTTextureTemplate.default())
            }
        }
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Netherite", "ネザライト")
                addTemplate(HTTextureTemplate.dull())
            }
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            if (isDataGen) {
                addName("Wood", "木")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.PLATE to "plate_wood"))
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(VanillaMaterialKeys.STONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Stone", "石")
                addTemplate(HTTextureTemplate.dull())
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
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
                addTemplate(HTTextureTemplate.dull())
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
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_fuel"))
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(CommonMaterialKeys.CARBIDE) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Carbide", "カーバイド")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_fuel"))
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        // Minerals
        event.modify(CommonMaterialKeys.CINNABAR) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Cinnabar", "辰砂")
                addTemplate(HTTextureTemplate.dull(HCMaterialPrefixes.STORAGE_BLOCK to "block_dust"))
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("crimson_crystal"))
            }
        }
        event.modify(CommonMaterialKeys.SALT) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Salt", "塩")
                addTemplate(HTTextureTemplate.shine(HCMaterialPrefixes.STORAGE_BLOCK to "block_dust"))
            }
        }
        event.modify(CommonMaterialKeys.SALTPETER) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Saltpeter", "硝石")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_dust"))
            }
        }
        event.modify(CommonMaterialKeys.SULFUR) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Sulfur", "硫黄")
                addTemplate(HTTextureTemplate.dull(HCMaterialPrefixes.STORAGE_BLOCK to "block_dust"))
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("gold"))
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
                addTemplate(
                    HTTextureTemplate.shine(HCMaterialPrefixes.STORAGE_BLOCK to "block_ingot"),
                )
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
                addTemplate(HTTextureTemplate.dull())
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
            }
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            addDefaultPart(HCMaterialPrefixes.PLATE)
            if (isDataGen) {
                addName("Plastic", "プラスチック")
                addTemplate(HTTextureTemplate.shine(HCMaterialPrefixes.STORAGE_BLOCK to "block_plate"))
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            addDefaultPart(HCMaterialPrefixes.PLATE)
            if (isDataGen) {
                addName("Rubber", "ゴム")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_plate"))
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
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
                addTemplate(
                    HTTextureTemplate.shine(
                        HCMaterialPrefixes.STORAGE_BLOCK to "block_gem",
                        HCMaterialPrefixes.GEM to "gem_amethyst",
                    ),
                )
            }
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_CRIMSON_CRYSTAL))
            if (isDataGen) {
                addName("Crimson Crystal", "深紅のクリスタリル")
                addTemplate(
                    HTTextureTemplate.default(
                        HCMaterialPrefixes.STORAGE_BLOCK to "block_gem_emerald",
                        HCMaterialPrefixes.GEM to "gem_emerald",
                    ),
                )
            }
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_WARPED_CRYSTAL))
            if (isDataGen) {
                addName("Warped Crystal", "歪んだクリスタリル")
                addTemplate(
                    HTTextureTemplate.default(
                        HCMaterialPrefixes.STORAGE_BLOCK to "block_gem_emerald",
                        HCMaterialPrefixes.GEM to "gem_emerald",
                    ),
                )
            }
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            addDefaultPart(HCMaterialPrefixes.PEARL)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_ELDRITCH))
            if (isDataGen) {
                addName("Eldritch Pearl", "異質な真珠")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_pearl"))
            }
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Azure Steel", "紺鉄")
                addTemplate(HTTextureTemplate.default(HCMaterialPrefixes.STORAGE_BLOCK to "block_ingot"))
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("azure"))
            }
        }
        event.modify(HCMaterialKeys.DEEP_STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Deep Steel", "深層鋼")
                addTemplate(HTTextureTemplate.dull(HCMaterialPrefixes.STORAGE_BLOCK to "block_ingot"))
            }
        }
    }
}
