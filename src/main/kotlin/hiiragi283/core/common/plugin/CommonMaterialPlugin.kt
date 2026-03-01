package hiiragi283.core.common.plugin

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.tool.CommonToolTypes
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTExtraOreResultMap
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialTextureSet
import hiiragi283.core.api.material.property.addBlockPrefixes
import hiiragi283.core.api.material.property.addCustomName
import hiiragi283.core.api.material.property.addFluidPrefixes
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.addToolPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.setup.HCToolMaterials
import net.minecraft.resources.ResourceLocation

@HTPlugin
object CommonMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = HTConst.COMMON.toId("material_plugin")

    override fun onModifyMaterial(builder: HTMaterialPlugin.MaterialBuilder) {
        fuel(builder)
        mineral(builder)
        gem(builder)
        metal(builder)
        alloy(builder)
        other(builder)
    }

    @JvmStatic
    private val materialBlockSet: Set<HTTagPrefix> = buildSet {
        addAll(CommonTagPrefixes.ORES)
        add(CommonTagPrefixes.RAW_BLOCK)
        add(CommonTagPrefixes.BLOCK)
    }

    @JvmStatic
    private val oreSet: Set<HTTagPrefix> = setOf(
        CommonTagPrefixes.DUST,
        CommonTagPrefixes.RAW,
        CommonTagPrefixes.CRUSHED_ORE,
    )

    @JvmStatic
    private val metalSet: Set<HTTagPrefix> = oreSet.plus(CommonTagPrefixes.INGOT).plus(CommonTagPrefixes.NUGGET)

    @JvmStatic
    private val alloySet: Set<HTTagPrefix> = metalSet.minus(CommonTagPrefixes.RAW).minus(CommonTagPrefixes.CRUSHED_ORE)

    @JvmStatic
    private val partSet: Set<HTTagPrefix> = setOf(CommonTagPrefixes.GEAR, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)

    @JvmStatic
    private fun fuel(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(CommonMaterialKeys.COAL_COKE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.FUEL, CommonTagPrefixes.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)

            setName("Coal Coke", "石炭コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
    }

    @JvmStatic
    private fun mineral(builder: HTMaterialPlugin.MaterialBuilder) {
        fun register(
            key: HTMaterialKey,
            enName: String,
            jaName: String,
            builderAction: HTPropertyMap.Mutable.() -> Unit = {},
        ) {
            builder.getBuilder(key).apply {
                setDefaultPart(HTDefaultPart.Prefixed.GEM)
                addBlockPrefixes(CommonTagPrefixes.ORES.plus(CommonTagPrefixes.RAW_BLOCK))
                addItemPrefixes(oreSet)

                setName(enName, jaName)
                setTextureSet("mineral", HTMaterialTextureSet.DULL)
                builderAction()
            }
        }

        register(CommonMaterialKeys.SALT, "Salt", "塩")
        register(CommonMaterialKeys.SALTPETER, "Saltpeter", "硝石")
        register(CommonMaterialKeys.BAUXITE, "Bauxite", "ボーキサイト")

        register(CommonMaterialKeys.SULFUR, "Sulfur", "硫黄")

        register(CommonMaterialKeys.PLATINUM_GROUP, "Platinum Group", "白金族") {
            setTextureSet("mineral", HTMaterialTextureSet.SHINE)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, HiiragiCoreAPI.id("black"))
        }

        register(CommonMaterialKeys.CINNABAR, "Cinnabar", "辰砂")

        register(CommonMaterialKeys.GALENA, "Galena", "方鉛鉱") {
            put(
                HTMaterialPropertyKeys.EXTRA_ORE_RESULTS,
                HTExtraOreResultMap.create {
                    crushOre(CommonMaterialKeys.SULFUR, 1 / 4f)
                    crushCrushed(CommonMaterialKeys.SILVER, 1 / 4f)
                    washCrushed(CommonMaterialKeys.SILVER, 1 / 2f)
                },
            )
            put(HTMaterialPropertyKeys.SMELTED_TO, CommonMaterialKeys.LEAD)
        }
    }

    @JvmStatic
    private fun gem(builder: HTMaterialPlugin.MaterialBuilder) {
        fun register(key: HTMaterialKey, enName: String, jaName: String) {
            builder.getBuilder(key).apply {
                setDefaultPart(HTDefaultPart.Prefixed.GEM)

                setName(enName, jaName)
            }
        }

        builder.getBuilder(CommonMaterialKeys.FLUORITE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER, fraction(3))

            setName("Fluorite", "蛍石")
        }
        register(CommonMaterialKeys.PERIDOT, "Peridot", "ペリドット")
        register(CommonMaterialKeys.RUBY, "Ruby", "ルビー")
        register(CommonMaterialKeys.SAPPHIRE, "Sapphire", "サファイア")
    }

    @JvmStatic
    private fun metal(builder: HTMaterialPlugin.MaterialBuilder) {
        fun register(
            key: HTMaterialKey,
            enName: String,
            jaName: String,
            level: HTMaterialLevel = HTMaterialLevel.MEDIUM,
        ) {
            builder.getBuilder(key).apply {
                setDefaultPart(HTDefaultPart.Prefixed.INGOT)
                put(HTMaterialPropertyKeys.HARDNESS, level)
                put(HTMaterialPropertyKeys.MELTING_POINT, level)

                setName(enName, jaName)
            }
        }

        fun platinumGroup(key: HTMaterialKey, enName: String, jaName: String) {
            builder.getBuilder(key).apply {
                setDefaultPart(HTDefaultPart.Prefixed.INGOT)
                addBlockPrefixes(CommonTagPrefixes.BLOCK)
                addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
                addItemPrefixes(alloySet)
                put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGHEST)
                put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGHEST)

                setName(enName, jaName)
                setTextureSet(HTMaterialTextureSet.MYSTICAL)
            }
        }
        // 2nd
        register(CommonMaterialKeys.LITHIUM, "Lithium", "リチウム", HTMaterialLevel.LOW)
        register(CommonMaterialKeys.BERYLLIUM, "Beryllium", "ベリリウム", HTMaterialLevel.HIGH)
        // 3rd
        register(CommonMaterialKeys.SODIUM, "Sodium", "ナトリウム", HTMaterialLevel.LOW)
        register(CommonMaterialKeys.MAGNESIUM, "Magnesium", "マグネシウム")

        builder.getBuilder(CommonMaterialKeys.ALUMINUM).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(alloySet.plus(CommonTagPrefixes.WIRE))
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Aluminum", "アルミニウム")
        }
        builder.getBuilder(CommonMaterialKeys.SILICON).apply {
            setDefaultPart(HiiragiCoreTags.Items.SILICON, null)

            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.HIGH)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.HIGH)

            setName("Silicon", "シリコン")
        }
        // 4th
        register(CommonMaterialKeys.TITANIUM, "Titanium", "チタン", HTMaterialLevel.HIGH)
        register(CommonMaterialKeys.VANADIUM, "Vanadium", "バナジウム")
        register(CommonMaterialKeys.CHROMIUM, "Chromium", "クロム")
        register(CommonMaterialKeys.MANGANESE, "Manganese", "マンガン")
        register(CommonMaterialKeys.COBALT, "Cobalt", "コバルト")
        builder.getBuilder(CommonMaterialKeys.NICKEL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Nickel", "ニッケル")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        builder.getBuilder(CommonMaterialKeys.ZINC).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(metalSet)

            setName("Zinc", "亜鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        // 5th
        register(CommonMaterialKeys.MOLYBDENUM, "Molybdenum", "モリブデン")
        platinumGroup(CommonMaterialKeys.RUTHENIUM, "Ruthenium", "ルテニウム")
        platinumGroup(CommonMaterialKeys.RHODIUM, "Rhodium", "ロジウム")
        platinumGroup(CommonMaterialKeys.PALLADIUM, "Palladium", "パラジウム")
        builder.getBuilder(CommonMaterialKeys.SILVER).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Silver", "銀")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(CommonMaterialKeys.TIN).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(metalSet)

            setName("Tin", "錫")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        register(CommonMaterialKeys.ANTIMONY, "Antimony", "アンチモン")
        // 6th
        register(CommonMaterialKeys.TUNGSTEN, "Tungsten", "パラジウム", HTMaterialLevel.HIGH)
        platinumGroup(CommonMaterialKeys.OSMIUM, "Osmium", "オスミウム")
        platinumGroup(CommonMaterialKeys.IRIDIUM, "Iridium", "イリジウム")
        platinumGroup(CommonMaterialKeys.PLATINUM, "Platinum", "白金")
        builder.getBuilder(CommonMaterialKeys.LEAD).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Lead", "鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        // 7th
        register(CommonMaterialKeys.URANIUM, "Uranium", "ウラン")
        register(CommonMaterialKeys.PLUTONIUM, "Plutonium", "プルトニウム")
    }

    @JvmStatic
    private fun alloy(builder: HTMaterialPlugin.MaterialBuilder) {
        fun register(
            key: HTMaterialKey,
            enName: String,
            jaName: String,
            level: HTMaterialLevel = HTMaterialLevel.MEDIUM,
        ) {
            builder.getBuilder(key).apply {
                setDefaultPart(HTDefaultPart.Prefixed.INGOT)
                put(HTMaterialPropertyKeys.HARDNESS, level)
                put(HTMaterialPropertyKeys.MELTING_POINT, level)

                setName(enName, jaName)
            }
        }

        builder.getBuilder(CommonMaterialKeys.STEEL).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.STEEL, CommonToolTypes.VANILLA_SET)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Steel", "鋼鉄")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        register(CommonMaterialKeys.INVAR, "Invar", "不変鋼")

        builder.getBuilder(CommonMaterialKeys.BRASS).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Brass", "真鍮")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        register(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        builder.getBuilder(CommonMaterialKeys.BRONZE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.BRONZE, CommonToolTypes.VANILLA_SET)

            setName("Bronze", "青銅")
        }

        register(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        register(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        register(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        register(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
    }

    @JvmStatic
    private fun other(builder: HTMaterialPlugin.MaterialBuilder) {
        builder.getBuilder(CommonMaterialKeys.ASH).apply {
            addItemPrefixes(CommonTagPrefixes.DUST)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Ash", "灰")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        builder.getBuilder(CommonMaterialKeys.CARBON).apply {
            addItemPrefixes(CommonTagPrefixes.DUST, CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Carbon", "炭素")
            // addCustomName(CommonTagPrefixes.WIRE, "Carbon Fiber", "炭素繊維")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, VanillaMaterialKeys.COAL.getId())
        }
        builder.getBuilder(CommonMaterialKeys.PLASTIC).apply {
            setDefaultPart(
                HiiragiCoreTags.Items.PLASTICS,
                HTSimpleDeferredItem(CommonTagPrefixes.PLATE.createId(CommonMaterialKeys.PLASTIC)),
            )
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.PLATE, CommonTagPrefixes.ROD)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Plastic", "プラスチック")
            // addCustomName(CommonTagPrefixes.DUST, "Plastic Pulp", "プラスチックパルプ")
            addCustomName(CommonTagPrefixes.INGOT, "Plastic Bar", "プラスチックバー")
            addCustomName(CommonTagPrefixes.PLATE, "Plastic Sheet", "プラスチックシート")
            // addCustomName(CommonTagPrefixes.WIRE, "Synthetic Fiber", "合成繊維")
            setTextureSet("polymer")
        }
        builder.getBuilder(CommonMaterialKeys.RUBBER).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonTagPrefixes.BLOCK)
            addFluidPrefixes(CommonFluidTagPrefixes.MOLTEN)
            addItemPrefixes(CommonTagPrefixes.INGOT, CommonTagPrefixes.PLATE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Rubber", "ゴム")
            // addCustomName(CommonTagPrefixes.DUST, "Rubber Pulp", "ゴムパルプ")
            addCustomName(CommonTagPrefixes.INGOT, "Rubber Bar", "ゴムバー")
            addCustomName(CommonTagPrefixes.PLATE, "Rubber Sheet", "ゴムシート")
            setTextureSet("polymer", HTMaterialTextureSet.DULL)
        }
    }
}
