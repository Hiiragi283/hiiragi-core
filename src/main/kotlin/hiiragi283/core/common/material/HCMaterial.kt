package hiiragi283.core.common.material

import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.material.HTAbstractMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixTemplateMap
import hiiragi283.core.common.data.texture.HCMaterialPalette
import hiiragi283.core.common.data.texture.HCMaterialPrefixMaps
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

sealed interface HCMaterial : HTAbstractMaterial {
    companion object {
        @JvmStatic
        val entries: Set<HCMaterial> get() = buildSet {
            addAll(Fuels.entries)
            addAll(Minerals.entries)
            addAll(Gems.entries)
            addAll(Pearls.entries)
            addAll(Metals.entries)
            addAll(Alloys.entries)
            addAll(Plates.entries)

            add(Wood)
            add(Stone)
            add(Obsidian)
        }

        private val dustSet: Set<HTMaterialPrefix> = setOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.TINY_DUST)
    }

    //    Fuels    //

    enum class Fuels(
        private val usName: String,
        private val jpName: String,
        override val colorPalette: HTColorPalette,
        private val isVanilla: Boolean = false,
    ) : HCMaterial {
        // Vanilla
        COAL("Coal", "石炭", HCMaterialPalette.COAL, true),
        CHARCOAL("Charcoal", "木炭", HCMaterialPalette.CHARCOAL, true),

        // Common
        COAL_COKE("Coal Coke", "石炭コークス", HCMaterialPalette.COAL_COKE),
        CARBIDE("Carbide", "カーバイド", HCMaterialPalette.CARBIDE),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.FUEL

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = buildSet {
            if (!isVanilla) {
                add(HCMaterialPrefixes.FUEL)
            }
            addAll(dustSet)
        }

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HCMaterialPrefixMaps.FUEL

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Minerals    //

    enum class Minerals(
        private val usName: String,
        private val jpName: String,
        override val colorPalette: HTColorPalette,
        private val isVanilla: Boolean = false,
    ) : HCMaterial {
        // Vanilla
        REDSTONE("Redstone", "赤石", HCMaterialPalette.REDSTONE, true),
        GLOWSTONE("Glowstone", "グロウストーン", HCMaterialPalette.GLOWSTONE, true),

        // Common
        CINNABAR("Cinnabar", "辰砂", HCMaterialPalette.CINNABAR),
        SALT("Salt", "塩", HCMaterialPalette.SALT),
        SALTPETER("Saltpeter", "硝石", HCMaterialPalette.SALTPETER),
        SULFUR("Sulfur", "硫黄", HCMaterialPalette.SULFUR),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.DUST

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = buildSet {
            if (!isVanilla) {
                add(HCMaterialPrefixes.DUST)
            }
            add(HCMaterialPrefixes.TINY_DUST)
        }

        override fun getItemPrefixMap(): HTPrefixTemplateMap = when {
            isVanilla -> HCMaterialPrefixMaps.DUST_SHINE
            this == SALT -> HCMaterialPrefixMaps.DUST_SHINE
            else -> HCMaterialPrefixMaps.DUST
        }

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Gems    //

    enum class Gems(
        private val usName: String,
        private val jpName: String,
        override val colorPalette: HTColorPalette,
        private val prefixMap: HTPrefixTemplateMap,
        private val isVanilla: Boolean = false,
    ) : HCMaterial {
        // Vanilla
        LAPIS("Lapis", "ラピス", HCMaterialPalette.LAPIS, HCMaterialPrefixMaps.GEM_LAPIS, true),
        QUARTZ("Quartz", "水晶", HCMaterialPalette.QUARTZ, HCMaterialPrefixMaps.GEM_QUARTZ, true),
        AMETHYST("Amethyst", "アメジスト", HCMaterialPalette.AMETHYST, HCMaterialPrefixMaps.GEM_AMETHYST, true),
        DIAMOND("Diamond", "ダイヤモンド", HCMaterialPalette.DIAMOND, HCMaterialPrefixMaps.GEM_DIAMOND, true),
        EMERALD("Emerald", "エメラルド", HCMaterialPalette.EMERALD, HCMaterialPrefixMaps.GEM_EMERALD, true),
        ECHO("Echo", "残響", HCMaterialPalette.ECHO, HCMaterialPrefixMaps.GEM_ECHO, true),

        // Hiiragi Series
        AZURE("Azure Shard", "紺碧の欠片", HCMaterialPalette.AZURE_STEEL, HCMaterialPrefixMaps.GEM_AMETHYST) {
            override fun createPath(prefix: HTMaterialPrefix): String = when (prefix) {
                HCMaterialPrefixes.GEM -> "azure_shard"
                else -> super.createPath(prefix)
            }
        },
        CRIMSON_CRYSTAL("Crimson Crystal", "深紅のクリスタリル", HCMaterialPalette.CRIMSON_CRYSTAL, HCMaterialPrefixMaps.GEM_EMERALD),
        WARPED_CRYSTAL("Warped Crystal", "歪んだクリスタリル", HCMaterialPalette.WARPED_CRYSTAL, HCMaterialPrefixMaps.GEM_EMERALD),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.GEM

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = buildSet {
            if (!isVanilla) {
                add(HCMaterialPrefixes.GEM)
            }
            addAll(dustSet)
        }

        override fun getItemPrefixMap(): HTPrefixTemplateMap = prefixMap

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
        ENDER("Ender Pearl", "エンダーパール", HCMaterialPalette.ENDER),

        // Hiiragi Series
        ELDRITCH("Eldritch Pearl", "異質な真珠", HCMaterialPalette.ELDRITCH),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.PEARL

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = when (this) {
            ENDER -> dustSet
            ELDRITCH -> buildSet {
                add(HCMaterialPrefixes.PEARL)
                addAll(dustSet)
            }
        }

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HCMaterialPrefixMaps.PEARL

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Metals    //

    enum class Metals(
        private val usName: String,
        private val jpName: String,
        override val colorPalette: HTColorPalette,
        private val prefixMap: HTPrefixTemplateMap,
        private val isVanilla: Boolean = false,
    ) : HCMaterial {
        // Vanilla
        COPPER("Copper", "銅", HCMaterialPalette.COPPER, HCMaterialPrefixMaps.METAL_SHINE, true),
        IRON("Iron", "鉄", HCMaterialPalette.IRON, HCMaterialPrefixMaps.METAL, true),
        GOLD("Gold", "金", HCMaterialPalette.GOLD, HCMaterialPrefixMaps.METAL_SHINE, true),

        // Common
        // ALUMINUM("Aluminum", "アルミニウム"),
        // NICKEL("Nickel", "ニッケル"),
        // ZINC("Zinc", "亜鉛"),
        SILVER("Silver", "銀", HCMaterialPalette.SILVER, HCMaterialPrefixMaps.METAL_SHINE),

        // TIN("Tin", "錫"),
        // PLATINUM("Platinum", "プラチナ"),
        // LEAD("Lead", "鉛"),
        // Hiiragi Series
        NIGHT_METAL("Night Metal", "夜金", HCMaterialPalette.NIGHT_METAL, HCMaterialPrefixMaps.METAL_SHINE),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.INGOT

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = buildSet {
            if (!isVanilla) {
                add(HCMaterialPrefixes.RAW_MATERIAL)
                add(HCMaterialPrefixes.INGOT)
            }
            add(HCMaterialPrefixes.DUST)
            add(HCMaterialPrefixes.TINY_DUST)
            add(HCMaterialPrefixes.GEAR)
            if (this@Metals == COPPER) {
                add(HCMaterialPrefixes.NUGGET)
            }
            if (!isVanilla) {
                add(HCMaterialPrefixes.NUGGET)
            }
            add(HCMaterialPrefixes.PLATE)
            add(HCMaterialPrefixes.ROD)
            add(HCMaterialPrefixes.WIRE)
        }

        override fun getItemPrefixMap(): HTPrefixTemplateMap = prefixMap

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
            override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = buildSet {
                addAll(super.getItemPrefixesToGenerate())
                remove(HCMaterialPrefixes.INGOT)
            }

            override fun getItemPrefixMap(): HTPrefixTemplateMap = HCMaterialPrefixMaps.METAL_SHINE
        },

        // Common
        STEEL("Steel", "鋼鉄", HCMaterialPalette.STEEL),

        // Hiiragi Series
        AZURE_STEEL("Azure Steel", "紺鉄", HCMaterialPalette.AZURE_STEEL),
        DEEP_STEEL("Deep Steel", "深層鋼", HCMaterialPalette.DEEP_STEEL) {
            override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = buildSet {
                add(HCMaterialPrefixes.SCRAP)
                addAll(super.getItemPrefixesToGenerate())
            }
        },
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.INGOT

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = setOf(
            HCMaterialPrefixes.INGOT,
            HCMaterialPrefixes.DUST,
            HCMaterialPrefixes.TINY_DUST,
            HCMaterialPrefixes.GEAR,
            HCMaterialPrefixes.NUGGET,
            HCMaterialPrefixes.PLATE,
            HCMaterialPrefixes.ROD,
            HCMaterialPrefixes.WIRE,
        )

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HCMaterialPrefixMaps.METAL

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
        RUBBER("Rubber", "ゴム", HCMaterialPalette.RUBBER),
        ;

        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.PLATE

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = setOf(
            HCMaterialPrefixes.RAW_MATERIAL,
            HCMaterialPrefixes.PLATE,
        )

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HTPrefixTemplateMap.create {
            addCustom(HCMaterialPrefixes.STORAGE_BLOCK, "block_plate")

            if (this@Plates == PLASTIC) {
                add(HCMaterialPrefixes.RAW_MATERIAL)
            }
            add(HCMaterialPrefixes.PLATE)
        }

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> usName
            HTLanguageType.JA_JP -> jpName
        }

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
    }

    //    Misc    //

    data object Wood : HCMaterial {
        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.DUST
        override val colorPalette: HTColorPalette = HCMaterialPalette.WOOD

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = setOf(
            HCMaterialPrefixes.DUST,
            HCMaterialPrefixes.TINY_DUST,
            HCMaterialPrefixes.PLATE,
        )

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HTPrefixTemplateMap.create {
            add(HCMaterialPrefixes.DUST)
            add(HCMaterialPrefixes.TINY_DUST)
            addCustom(HCMaterialPrefixes.PLATE, "plate_wooden")
        }

        override fun getBaseIngredient(): TagKey<Item> = ItemTags.PLANKS

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of("wood")

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Wood"
            HTLanguageType.JA_JP -> "木"
        }
    }

    data object Stone : HCMaterial {
        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.DUST
        override val colorPalette: HTColorPalette = HCMaterialPalette.STONE

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = setOf(
            HCMaterialPrefixes.DUST,
            HCMaterialPrefixes.TINY_DUST,
            HCMaterialPrefixes.PLATE,
            HCMaterialPrefixes.ROD,
        )

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HTPrefixTemplateMap.create {
            addCustom(HCMaterialPrefixes.DUST, "dust_dull")
            add(HCMaterialPrefixes.TINY_DUST)
            addCustom(HCMaterialPrefixes.PLATE, "plate_wooden")
            add(HCMaterialPrefixes.ROD)
        }

        override fun getBaseIngredient(): TagKey<Item> = ItemTags.STONE_CRAFTING_MATERIALS

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of("stone")

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Stone"
            HTLanguageType.JA_JP -> "石"
        }
    }

    data object Obsidian : HCMaterial {
        override val basePrefix: HTMaterialPrefix = HCMaterialPrefixes.DUST
        override val colorPalette: HTColorPalette = HCMaterialPalette.OBSIDIAN

        override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = dustSet

        override fun getItemPrefixMap(): HTPrefixTemplateMap = HTPrefixTemplateMap.create {
            add(HCMaterialPrefixes.DUST)
            add(HCMaterialPrefixes.TINY_DUST)
        }

        override fun getBaseIngredient(): TagKey<Item> = Tags.Items.OBSIDIANS

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of("obsidian")

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Obsidian"
            HTLanguageType.JA_JP -> "黒曜石"
        }
    }
}
