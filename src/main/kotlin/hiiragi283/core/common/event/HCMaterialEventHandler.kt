package hiiragi283.core.common.event

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.HTSmeltingMaterialProperty
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addDefaultPart
import hiiragi283.core.api.material.property.addName
import hiiragi283.core.api.material.property.addTextureSet
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.world.item.Items
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

    private val smeltingToAsh: HTSmeltingMaterialProperty by lazy {
        HTSmeltingMaterialProperty.smeltingOnly(HCItems.MATERIALS.getOrThrow(HCMaterialPrefixes.DUST, CommonMaterialKeys.ASH))
    }

    @JvmStatic
    private fun vanilla(event: HTMaterialPropertyEvent) {
        val isDataGen: Boolean = event.isDataGen
        // Fuels
        event.modify(VanillaMaterialKeys.COAL) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Coal", "石炭")
                addTextureSet("fuel")
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(VanillaMaterialKeys.CHARCOAL) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Charcoal", "木炭")
                addTextureSet("fuel")
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
                addTextureSet("lapis")
            }
        }
        event.modify(VanillaMaterialKeys.QUARTZ) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Quartz", "水晶")
                addTextureSet("quartz")
            }
        }
        event.modify(VanillaMaterialKeys.AMETHYST) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            if (isDataGen) {
                addName("Amethyst", "アメジスト")
                addTextureSet("amethyst")
            }
        }
        event.modify(VanillaMaterialKeys.DIAMOND) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Diamond", "ダイヤモンド")
                addTextureSet("diamond")
            }
        }
        event.modify(VanillaMaterialKeys.EMERALD) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Emerald", "エメラルド")
                addTextureSet("emerald")
            }
        }
        event.modify(VanillaMaterialKeys.ECHO) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            if (isDataGen) {
                addName("Echo Shard", "残響の欠片")
                addTextureSet("echo")
            }
        }
        // Pearls
        event.modify(VanillaMaterialKeys.ENDER) {
            addDefaultPart(HCMaterialPrefixes.PEARL)
            if (isDataGen) {
                addName("Ender Pearl", "エンダーパール")
                addTextureSet("pearl")
            }
        }
        // Metals
        event.modify(VanillaMaterialKeys.COPPER) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Copper", "銅")
                addTextureSet("shine")
            }
        }
        event.modify(VanillaMaterialKeys.IRON) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Iron", "鉄")
            }
        }
        event.modify(VanillaMaterialKeys.GOLD) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Gold", "金")
            }
        }
        // Alloys
        event.modify(VanillaMaterialKeys.NETHERITE) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Netherite", "ネザライト")
                addTextureSet("dull")
            }
        }
        // Others
        event.modify(VanillaMaterialKeys.WOOD) {
            if (isDataGen) {
                addName("Wood", "木")
                addCustomName(HCMaterialPrefixes.DUST, "Sawdust", "おがくず")
                addTextureSet("wood")
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(VanillaMaterialKeys.GLASS) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_GLASS))
            if (isDataGen) {
                addName("Glass", "ガラス")
                addTextureSet("shine")
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.smeltingOnly(Items.GLASS.toHolderLike()))
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("salt"))
            }
        }
        event.modify(VanillaMaterialKeys.STONE) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Stone", "石")
                addTextureSet("dull")
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
            }
        }
        event.modify(VanillaMaterialKeys.OBSIDIAN) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            put(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT)
            if (isDataGen) {
                addName("Obsidian", "黒曜石")
                addTextureSet("dull")
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
            }
        }
    }

    @JvmStatic
    private fun common(event: HTMaterialPropertyEvent) {
        val isDataGen: Boolean = event.isDataGen

        fun registerGem(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                addDefaultPart(HCMaterialPrefixes.GEM)
                if (isDataGen) {
                    addName(enName, jaName)
                }
            }
        }

        fun registerMetal(key: HTMaterialKey, enName: String, jaName: String) {
            event.modify(key) {
                addDefaultPart(HCMaterialPrefixes.INGOT)
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
                addTextureSet("fuel")
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(CommonMaterialKeys.CARBIDE) {
            addDefaultPart(HCMaterialPrefixes.FUEL)
            if (isDataGen) {
                addName("Carbide", "カーバイド")
                addTextureSet("fuel")
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        // Minerals
        event.modify(CommonMaterialKeys.CINNABAR) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Cinnabar", "辰砂")
                addTextureSet("mineral", HTMaterialTextureSet.DULL)
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("crimson_crystal"))
            }
        }
        event.modify(CommonMaterialKeys.SALT) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Salt", "塩")
                addTextureSet("mineral")
            }
        }
        event.modify(CommonMaterialKeys.SALTPETER) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Saltpeter", "硝石")
                addTextureSet("mineral")
            }
        }
        event.modify(CommonMaterialKeys.SULFUR) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Sulfur", "硫黄")
                addTextureSet("mineral", HTMaterialTextureSet.DULL)
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("gold"))
            }
        }
        // Gems
        registerGem(CommonMaterialKeys.FLUORITE, "Fluorite", "蛍石")
        registerGem(CommonMaterialKeys.PERIDOT, "Peridot", "ペリドット")
        registerGem(CommonMaterialKeys.RUBY, "Ruby", "ルビー")
        registerGem(CommonMaterialKeys.SAPPHIRE, "Sapphire", "サファイア")
        // Metals
        registerMetal(CommonMaterialKeys.ALUMINUM, "Aluminum", "アルミニウム")

        registerMetal(CommonMaterialKeys.TITANIUM, "Titanium", "チタン")
        registerMetal(CommonMaterialKeys.CHROME, "Chrome", "クロム")
        registerMetal(CommonMaterialKeys.CHROMIUM, "Chromium", "クロム")
        registerMetal(CommonMaterialKeys.MANGANESE, "Manganese", "マンガン")
        registerMetal(CommonMaterialKeys.COBALT, "Cobalt", "コバルト")
        registerMetal(CommonMaterialKeys.NICKEL, "Nickel", "ニッケル")
        registerMetal(CommonMaterialKeys.ZINC, "Zinc", "亜鉛")

        registerMetal(CommonMaterialKeys.PALLADIUM, "Palladium", "パラジウム")
        registerMetal(CommonMaterialKeys.SILVER, "Silver", "銀")
        registerMetal(CommonMaterialKeys.TIN, "Tin", "錫")
        registerMetal(CommonMaterialKeys.ANTIMONY, "Antimony", "アンチモン")

        registerMetal(CommonMaterialKeys.TUNGSTEN, "Tungsten", "パラジウム")
        registerMetal(CommonMaterialKeys.OSMIUM, "Osmium", "オスミウム")
        registerMetal(CommonMaterialKeys.IRIDIUM, "Iridium", "イリジウム")
        registerMetal(CommonMaterialKeys.PLATINUM, "Platinum", "白金")
        registerMetal(CommonMaterialKeys.LEAD, "Lead", "鉛")

        registerMetal(CommonMaterialKeys.URANIUM, "Uranium", "ウラン")
        // Alloys
        event.modify(CommonMaterialKeys.STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Steel", "鋼鉄")
                addTextureSet("shine")
            }
        }
        registerMetal(CommonMaterialKeys.INVAR, "Invar", "不変鋼")
        registerMetal(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        registerMetal(CommonMaterialKeys.BRASS, "Brass", "真鍮")
        registerMetal(CommonMaterialKeys.BRONZE, "Bronze", "青銅")
        registerMetal(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        registerMetal(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        registerMetal(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        registerMetal(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
        // Others
        event.modify(CommonMaterialKeys.ASH) {
            addDefaultPart(HCMaterialPrefixes.DUST)
            if (isDataGen) {
                addName("Ash", "灰")
                addTextureSet("dull")
                put(HTMaterialPropertyKeys.SMELTING, HTSmeltingMaterialProperty.disable())
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("steel"))
            }
        }
        event.modify(CommonMaterialKeys.PLASTIC) {
            addDefaultPart(HCMaterialPrefixes.PLATE)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_PLASTIC))
            if (isDataGen) {
                addName("Plastic", "プラスチック")
                addTextureSet("plate", HTMaterialTextureSet.SHINE)
                put(HTMaterialPropertyKeys.SMELTING, smeltingToAsh)
            }
        }
        event.modify(CommonMaterialKeys.RUBBER) {
            addDefaultPart(HCMaterialPrefixes.PLATE)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_RUBBER))
            if (isDataGen) {
                addName("Rubber", "ゴム")
                addCustomName(HCMaterialPrefixes.PLATE, "Rubber Sheet", "ゴムシート")
                addTextureSet("plate")
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
                addTextureSet("amethyst", HTMaterialTextureSet.SHINE)
            }
        }
        event.modify(HCMaterialKeys.CRIMSON_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_CRIMSON_CRYSTAL))
            if (isDataGen) {
                addName("Crimson Crystal", "深紅のクリスタリル")
                addTextureSet("emerald")
            }
        }
        event.modify(HCMaterialKeys.WARPED_CRYSTAL) {
            addDefaultPart(HCMaterialPrefixes.GEM)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_WARPED_CRYSTAL))
            if (isDataGen) {
                addName("Warped Crystal", "歪んだクリスタリル")
                addTextureSet("emerald")
            }
        }
        // Pearls
        event.modify(HCMaterialKeys.ELDRITCH) {
            addDefaultPart(HCMaterialPrefixes.PEARL)
            put(HTMaterialPropertyKeys.MOLTEN_FLUID, HTFluidMaterialProperty(HCFluids.MOLTEN_ELDRITCH))
            if (isDataGen) {
                addName("Eldritch Pearl", "異質な真珠")
                addTextureSet("pearl")
            }
        }
        // Alloys
        event.modify(HCMaterialKeys.AZURE_STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Azure Steel", "紺鉄")
                put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("azure"))
            }
        }
        event.modify(HCMaterialKeys.DEEP_STEEL) {
            addDefaultPart(HCMaterialPrefixes.INGOT)
            if (isDataGen) {
                addName("Deep Steel", "深層鋼")
                addTextureSet("dull")
            }
        }
    }
}
