package hiiragi283.core.common.material

import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
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

    fun getSupportedItemPrefixes(): List<HTMaterialPrefix>

    fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = getSupportedItemPrefixes()

    fun createPath(prefix: HTMaterialPrefix): String = prefix.createPath(this)

    fun getBaseIngredient(): TagKey<Item> = basePrefix.itemTagKey(this)

    //    Fuels    //

    enum class Fuels(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        COAL("Coal", "石炭") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        CHARCOAL("Charcoal", "木炭") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },

        // Common
        COAL_COKE("Coal Coke", "石炭コークス"),
        CARBIDE("Carbide", "カーバイド"),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.FUEL

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.FUEL,
            HCMaterialPrefixes.DUST,
        )

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Gems    //

    enum class Gems(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        LAPIS("Lapis", "ラピス") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        QUARTZ("Quartz", "水晶") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        AMETHYST("Amethyst", "アメジスト") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        DIAMOND("Diamond", "ダイヤモンド") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        EMERALD("Emerald", "エメラルド") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },
        ECHO("Echo", "残響") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },

        // Common
        CINNABAR("Cinnabar", "辰砂"),
        SALTPETER("Saltpeter", "硝石"),
        SULFUR("Sulfur", "硫黄"),

        // Hiiragi Series
        AZURE("Azure Shard", "紺碧の欠片") {
            override fun createPath(prefix: HTMaterialPrefix): String = when (prefix) {
                HCMaterialPrefixes.GEM -> "azure_shard"
                else -> super.createPath(prefix)
            }
        },
        CRIMSON_CRYSTAL("Crimson Crystal", "深紅のクリスタリル"),
        WARPED_CRYSTAL("Warped Crystal", "歪んだクリスタリル"),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.GEM

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.GEM,
            HCMaterialPrefixes.DUST,
        )

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Pearls    //

    enum class Pearls(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        ENDER("Ender", "エンダー") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = dustList
        },

        // Hiiragi Series
        ELDRITCH("Eldritch", "エルドリッチ"),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.PEARL

        override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = listOf(
            HCMaterialPrefixes.PEARL,
            HCMaterialPrefixes.DUST,
        )

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Metals    //

    enum class Metals(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        COPPER("Copper", "銅") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(HCMaterialPrefixes.RAW_MATERIAL)
                remove(HCMaterialPrefixes.INGOT)
            }
        },
        IRON("Iron", "鉄") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(HCMaterialPrefixes.RAW_MATERIAL)
                remove(HCMaterialPrefixes.INGOT)
                remove(HCMaterialPrefixes.NUGGET)
            }
        },
        GOLD("Gold", "金") {
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
        // SILVER("silver", "銀"),
        // TIN("Tin", "錫"),
        // PLATINUM("Platinum", "プラチナ"),
        // LEAD("Lead", "鉛"),
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

    enum class Alloys(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        NETHERITE("Netherite", "ネザライト") {
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
        STEEL("Steel", "鋼鉄"),

        // Hiiragi Series
        AZURE_STEEL("Azure Steel", "紺鉄"),
        DEEP_STEEL("Deep Steel", "深層鋼") {
            override fun getSupportedItemPrefixes(): List<HTMaterialPrefix> = buildList {
                add(HCMaterialPrefixes.SCRAP)
                addAll(super.getSupportedItemPrefixes())
            }

            override fun createPath(prefix: HTMaterialPrefix): String = when (prefix) {
                HCMaterialPrefixes.SCRAP -> "deep_scrap"
                else -> super.createPath(prefix)
            }
        },
        NIGHT_METAL("Night Metal", "夜金"),
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

    enum class Dusts(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        WOOD("Wood", "木") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf(
                HCMaterialPrefixes.DUST,
                HCMaterialPrefixes.PLATE,
            )

            override fun getBaseIngredient(): TagKey<Item> = ItemTags.PLANKS
        },
        STONE("Stone", "石") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf()

            override fun getBaseIngredient(): TagKey<Item> = ItemTags.STONE_CRAFTING_MATERIALS
        },
        REDSTONE("Redstone", "赤石") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf()
        },
        GLOWSTONE("Glowstone", "グロウストーン") {
            override fun getItemPrefixesToGenerate(): List<HTMaterialPrefix> = listOf()
        },
        OBSIDIAN("Obsidian", "黒曜石") {
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

    enum class Plates(private val usName: String, private val jpName: String) : HCMaterial {
        // Common
        PLASTIC("Plastic", "プラスチック"),
        RUBBER("Rubber", "ゴム"),
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
