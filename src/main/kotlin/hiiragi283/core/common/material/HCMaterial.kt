package hiiragi283.core.common.material

import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.world.level.block.state.BlockBehaviour

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

        private val dustList: List<CommonMaterialPrefixes> = listOf(CommonMaterialPrefixes.DUST)
    }

    val basePrefix: HTPrefixLike

    fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes>

    fun getStorageBlockProp(): BlockBehaviour.Properties? = null

    fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = getSupportedItemPrefixes()

    fun createPath(prefix: CommonMaterialPrefixes): String = prefix.idPattern.replace("%s", asMaterialName())

    //    Fuels    //

    enum class Fuels(private val usName: String, private val jpName: String) : HCMaterial {
        // Vanilla
        COAL("Coal", "石炭") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },
        CHARCOAL("Charcoal", "木炭") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },

        // Common
        COAL_COKE("Coal Coke", "石炭コークス"),
        CARBIDE("Carbide", "カーバイド"),
        ;

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.FUEL

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = listOf(
            CommonMaterialPrefixes.FUEL,
            CommonMaterialPrefixes.DUST,
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
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },
        QUARTZ("Quartz", "水晶") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },
        AMETHYST("Amethyst", "アメジスト") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },
        DIAMOND("Diamond", "ダイアモンド") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },
        EMERALD("Emerald", "エメラルド") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },
        ECHO("Echo", "残響") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },

        // Common
        CINNABAR("Cinnabar", "辰砂"),
        SALTPETER("Saltpeter", "硝石"),
        SULFUR("Sulfur", "硫黄"),

        // Hiiragi Series
        AZURE("Azure Shard", "紺碧の欠片"),
        CRIMSON_CRYSTAL("Crimson Crystal", "深紅のクリスタリル"),
        WARPED_CRYSTAL("Warped Crystal", "歪んだクリスタリル"),
        ;

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.GEM

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = listOf(
            CommonMaterialPrefixes.GEM,
            CommonMaterialPrefixes.DUST,
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
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = dustList
        },

        // Hiiragi Series
        ELDRITCH("Eldritch", "異質な"),
        ;

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.PEARL

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = listOf(
            CommonMaterialPrefixes.PEARL,
            CommonMaterialPrefixes.DUST,
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
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(CommonMaterialPrefixes.RAW_MATERIAL)
                remove(CommonMaterialPrefixes.INGOT)
            }
        },
        IRON("Iron", "鉄") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(CommonMaterialPrefixes.RAW_MATERIAL)
                remove(CommonMaterialPrefixes.INGOT)
                remove(CommonMaterialPrefixes.NUGGET)
            }
        },
        GOLD("Gold", "金") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(CommonMaterialPrefixes.RAW_MATERIAL)
                remove(CommonMaterialPrefixes.INGOT)
                remove(CommonMaterialPrefixes.NUGGET)
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

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.INGOT

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = listOf(
            CommonMaterialPrefixes.RAW_MATERIAL,
            CommonMaterialPrefixes.INGOT,
            CommonMaterialPrefixes.DUST,
            CommonMaterialPrefixes.GEAR,
            CommonMaterialPrefixes.NUGGET,
            CommonMaterialPrefixes.PLATE,
            CommonMaterialPrefixes.ROD,
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
            override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = buildList {
                add(CommonMaterialPrefixes.SCRAP)
                addAll(super.getSupportedItemPrefixes())
            }

            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = buildList {
                addAll(super.getSupportedItemPrefixes())
                remove(CommonMaterialPrefixes.INGOT)
            }
        },

        // Common
        STEEL("Steel", "鋼鉄"),

        // Hiiragi Series
        AZURE_STEEL("Azure Steel", "紺鉄"),
        DEEP_STEEL("Deep Steel", "深層鋼") {
            override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = buildList {
                add(CommonMaterialPrefixes.SCRAP)
                addAll(super.getSupportedItemPrefixes())
            }

            override fun createPath(prefix: CommonMaterialPrefixes): String = when (prefix) {
                CommonMaterialPrefixes.SCRAP -> "deep_scrap"
                else -> super.createPath(prefix)
            }
        },
        NIGHT_METAL("Night Metal", "夜金"),
        ;

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.INGOT

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = listOf(
            CommonMaterialPrefixes.INGOT,
            CommonMaterialPrefixes.DUST,
            CommonMaterialPrefixes.GEAR,
            CommonMaterialPrefixes.NUGGET,
            CommonMaterialPrefixes.PLATE,
            CommonMaterialPrefixes.ROD,
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
        REDSTONE("Redstone", "赤石") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = listOf()
        },
        GLOWSTONE("Glowstone", "グロウストーン") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = listOf()
        },
        OBSIDIAN("Obsidian", "黒曜石"),
        WOOD("Wood", "木") {
            override fun getItemPrefixesToGenerate(): List<CommonMaterialPrefixes> = listOf(
                CommonMaterialPrefixes.DUST,
                CommonMaterialPrefixes.PLATE,
            )

            override fun createPath(prefix: CommonMaterialPrefixes): String = when (prefix) {
                CommonMaterialPrefixes.DUST -> "sawdust"
                else -> super.createPath(prefix)
            }
        },
        ;

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.DUST

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = dustList

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
        RAW_RUBBER("Raw Rubber", "生ゴム"),
        RUBBER("Rubber", "ゴム"),
        ;

        override val basePrefix: HTPrefixLike = CommonMaterialPrefixes.PLATE

        override fun getSupportedItemPrefixes(): List<CommonMaterialPrefixes> = listOf(CommonMaterialPrefixes.PLATE)

        override fun createPath(prefix: CommonMaterialPrefixes): String = when (this) {
            PLASTIC -> "plastic_plate"
            else -> "${asMaterialName()}_sheet"
        }

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }
}
