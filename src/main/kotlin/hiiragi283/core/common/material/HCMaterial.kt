package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.data.texture.HCMaterialPalette
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

sealed interface HCMaterial : HTMaterialLike.Translatable {
    companion object {
        @JvmStatic
        val entries: List<HCMaterial> by lazy {
            buildList {
                addAll(Fuels.entries)
                addAll(Gems.entries)
                addAll(Pearls.entries)
                addAll(Metals.entries)
                addAll(Alloys.entries)
                addAll(Dusts.entries)
                addAll(Plates.entries)
            }
        }

        @JvmStatic
        fun getPrefixedEntries(): List<Pair<HTMaterialPrefix, HCMaterial>> = entries.map { it.basePrefix to it }

        private val dustList: List<HTMaterialPrefix> = listOf(HCMaterialPrefixes.DUST)
    }

    val basePrefix: HTMaterialPrefix
    val colorPalette: HTColorPalette?

    fun getSupportedItemPrefixes(): List<HTMaterialPrefix>

    fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = getSupportedItemPrefixes()

    fun createPath(prefix: HTMaterialPrefix): String = prefix.createPath(this)

    fun getBaseIngredient(): TagKey<Item> = basePrefix.itemTagKey(this)

    fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = HiiragiCoreAPI.id("template", prefix.name)

    //    Fuels    //

    enum class Fuels(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
        HCMaterial {
        // Vanilla
        COAL("Coal", "石炭", HCMaterialPalette.COAL) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        CHARCOAL("Charcoal", "木炭", HCMaterialPalette.CHARCOAL) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },

        // Common
        COAL_COKE("Coal Coke", "石炭コークス", HCMaterialPalette.COAL_COKE),
        CARBIDE("Carbide", "カーバイド", HCMaterialPalette.CARBIDE),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.FUEL

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.FUEL,
            HCMaterialPrefixes.DUST,
        )

        override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
            HCMaterialPrefixes.DUST -> HiiragiCoreAPI.id("template", "dust_dull")
            else -> super.getTemplateId(prefix)
        }

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Gems    //

    enum class Gems(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
        HCMaterial {
        // Vanilla
        LAPIS("Lapis", "ラピス", HCMaterialPalette.LAPIS) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        QUARTZ("Quartz", "水晶", HCMaterialPalette.QUARTZ) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        AMETHYST("Amethyst", "アメジスト", HCMaterialPalette.AMETHYST) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        DIAMOND("Diamond", "ダイヤモンド", HCMaterialPalette.DIAMOND) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        EMERALD("Emerald", "エメラルド", HCMaterialPalette.EMERALD) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        ECHO("Echo", "残響", HCMaterialPalette.ECHO) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },

        // Common
        CINNABAR("Cinnabar", "辰砂", HCMaterialPalette.CINNABAR),
        SALTPETER("Saltpeter", "硝石", HCMaterialPalette.SALTPETER),
        SULFUR("Sulfur", "硫黄", HCMaterialPalette.SULFUR),

        // Hiiragi Series
        AZURE("Azure Shard", "紺碧の欠片", HCMaterialPalette.AZURE_STEEL) {
            override fun createPath(prefix: HTMaterialPrefix): String = when (prefix) {
                HCMaterialPrefixes.GEM -> "azure_shard"
                else -> super.createPath(prefix)
            }
        },
        CRIMSON_CRYSTAL("Crimson Crystal", "深紅のクリスタリル", HCMaterialPalette.CRIMSON_CRYSTAL),
        WARPED_CRYSTAL("Warped Crystal", "歪んだクリスタリル", HCMaterialPalette.WARPED_CRYSTAL),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.GEM

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.GEM,
            HCMaterialPrefixes.DUST,
        )

        override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
            HCMaterialPrefixes.GEM -> null
            else -> super.getTemplateId(prefix)
        }

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Pearls    //

    enum class Pearls(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
        HCMaterial {
        // Vanilla
        ENDER("Ender Pearl", "エンダーパール", HCMaterialPalette.ENDER) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },

        // Hiiragi Series
        ELDRITCH("Eldritch Pearl", "異質な真珠", HCMaterialPalette.ELDRITCH),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.PEARL

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.PEARL,
            HCMaterialPrefixes.DUST,
        )

        override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
            HCMaterialPrefixes.PEARL -> null
            else -> super.getTemplateId(prefix)
        }

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Metals    //

    enum class Metals(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
        HCMaterial {
        // Vanilla
        COPPER("Copper", "銅", HCMaterialPalette.COPPER) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(HCMaterialPrefixes.RAW_MATERIAL)
                remove(HCMaterialPrefixes.INGOT)
            }
        },
        IRON("Iron", "鉄", HCMaterialPalette.IRON) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(HCMaterialPrefixes.RAW_MATERIAL)
                remove(HCMaterialPrefixes.INGOT)
                remove(HCMaterialPrefixes.NUGGET)
            }
        },
        GOLD("Gold", "金", HCMaterialPalette.GOLD) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(HCMaterialPrefixes.RAW_MATERIAL)
                remove(HCMaterialPrefixes.INGOT)
                remove(HCMaterialPrefixes.NUGGET)
            }
        },

        // Common
        // ALUMINUM("Aluminum", "アルミニウム"),
        // NICKEL("Nickel", "ニッケル"),
        // ZINC("Zinc", "亜鉛"),
        SILVER("Silver", "銀", HCMaterialPalette.SILVER),

        // TIN("Tin", "錫"),
        // PLATINUM("Platinum", "プラチナ"),
        // LEAD("Lead", "鉛"),
        // Hiiragi Series
        NIGHT_METAL("Night Metal", "夜金", HCMaterialPalette.NIGHT_METAL),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.INGOT

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.RAW_MATERIAL,
            HCMaterialPrefixes.INGOT,
            HCMaterialPrefixes.DUST,
            HCMaterialPrefixes.GEAR,
            HCMaterialPrefixes.NUGGET,
            HCMaterialPrefixes.PLATE,
            HCMaterialPrefixes.ROD,
            HCMaterialPrefixes.WIRE,
        )

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Alloys    //

    enum class Alloys(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
        HCMaterial {
        // Vanilla
        NETHERITE("Netherite", "ネザライト", HCMaterialPalette.NETHERITE) {
            override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = buildList {
                add(HCMaterialPrefixes.SCRAP)
                addAll(super.getSupportedItemPrefixes())
            }

            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(HCMaterialPrefixes.INGOT)
            }
        },

        // Common
        STEEL("Steel", "鋼鉄", HCMaterialPalette.STEEL),

        // Hiiragi Series
        AZURE_STEEL("Azure Steel", "紺鉄", HCMaterialPalette.AZURE_STEEL) {
            override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
                HCMaterialPrefixes.DUST -> HiiragiCoreAPI.id("template", "dust_dull")
                else -> super.getTemplateId(prefix)
            }
        },
        DEEP_STEEL("Deep Steel", "深層鋼", HCMaterialPalette.DEEP_STEEL) {
            override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = buildList {
                add(HCMaterialPrefixes.SCRAP)
                addAll(super.getSupportedItemPrefixes())
            }

            override fun createPath(prefix: HTMaterialPrefix): String = when (prefix) {
                HCMaterialPrefixes.SCRAP -> "deep_scrap"
                else -> super.createPath(prefix)
            }
        },
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.INGOT

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.INGOT,
            HCMaterialPrefixes.DUST,
            HCMaterialPrefixes.GEAR,
            HCMaterialPrefixes.NUGGET,
            HCMaterialPrefixes.PLATE,
            HCMaterialPrefixes.ROD,
            HCMaterialPrefixes.WIRE,
        )

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Dusts    //

    enum class Dusts(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette? = null) :
        HCMaterial {
        // Vanilla
        WOOD("Wood", "木", HCMaterialPalette.WOOD) {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf(
                HCMaterialPrefixes.DUST,
                HCMaterialPrefixes.PLATE,
            )

            override fun getBaseIngredient(): TagKey<Item> = ItemTags.PLANKS

            override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
                HCMaterialPrefixes.PLATE -> null
                else -> super.getTemplateId(prefix)
            }
        },
        STONE("Stone", "石", HCMaterialPalette.STONE) {
            override fun getBaseIngredient(): TagKey<Item> = ItemTags.STONE_CRAFTING_MATERIALS

            override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
                HCMaterialPrefixes.DUST -> HiiragiCoreAPI.id("template", "dust_dull")
                else -> super.getTemplateId(prefix)
            }
        },
        REDSTONE("Redstone", "赤石") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf()
        },
        GLOWSTONE("Glowstone", "グロウストーン") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf()
        },
        OBSIDIAN("Obsidian", "黒曜石", HCMaterialPalette.OBSIDIAN) {
            override fun getBaseIngredient(): TagKey<Item> = Tags.Items.OBSIDIANS
        },
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.DUST

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = dustList

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Plates    //

    enum class Plates(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
        HCMaterial {
        // Common
        PLASTIC("Plastic", "プラスチック", HCMaterialPalette.PLASTIC),
        RUBBER("Rubber", "ゴム", HCMaterialPalette.RUBBER) {
            override fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? = when (prefix) {
                HCMaterialPrefixes.RAW_MATERIAL -> null
                else -> super.getTemplateId(prefix)
            }
        },
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.PLATE

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.RAW_MATERIAL,
            HCMaterialPrefixes.PLATE,
        )

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }
}
