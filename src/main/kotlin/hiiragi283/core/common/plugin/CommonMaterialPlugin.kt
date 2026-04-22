package hiiragi283.core.common.plugin

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.div
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.item.tool.VanillaToolTypes
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.part.property.addNamePattern
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
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.add
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.times
import hiiragi283.core.common.item.tool.CommonToolTypes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCToolMaterials
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import org.apache.commons.lang3.math.Fraction

@HTPlugin
object CommonMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = -1000

    override fun getId(): ResourceLocation = HTConst.COMMON.toId("material_plugin")

    override fun registerPart(registrar: HTMaterialPlugin.PartRegistrar) {
        blockPart(registrar)
        itemPart(registrar)
    }

    @JvmStatic
    private val toolTypes: Set<HTToolType> = VanillaToolTypes.VANILLA_SET.plus(CommonToolTypes.HAMMER)

    @JvmStatic
    private fun blockPart(registrar: HTMaterialPlugin.PartRegistrar) {
        fun registerOre(
            name: String,
            enPrefix: String,
            jaPrefix: String,
            properties: BlockBehaviour.Properties,
            stoneTexture: ResourceLocation,
        ): HTPartLike = registrar.register("ore/$name", "${name}_%s_ore") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.ORE)
            add(HTPartPropertyKeys.IS_ORE)
            add(HTPartPropertyKeys.IS_RAW)

            put(HTPartPropertyKeys.BLOCK_PROP, properties)
            put(HTPartPropertyKeys.ORE_STONE_TEX, stoneTexture)

            addNamePattern("$enPrefix %s Ore", "$jaPrefix%s鉱石")
            add(HTPartPropertyKeys.DISABLE_TEXTURE_GEN)
        }

        registrar.register("ore", "%s_ore") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.ORE)
            add(HTPartPropertyKeys.IS_ORE)
            add(HTPartPropertyKeys.IS_RAW)

            put(
                HTPartPropertyKeys.BLOCK_PROP,
                BlockBehaviour.Properties
                    .of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3f, 3f),
            )
            put(HTPartPropertyKeys.ORE_STONE_TEX, HTConst.MINECRAFT.toId(HTConst.BLOCK, "stone"))

            addNamePattern("%s Ore", "%s鉱石")
        }
        registerOre(
            "deepslate",
            "Deepslate",
            "深層",
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.DEEPSLATE)
                .requiresCorrectToolForDrops()
                .strength(4.5f, 3f)
                .sound(SoundType.DEEPSLATE),
            HTConst.MINECRAFT.toId(HTConst.BLOCK, "deepslate"),
        )
        registerOre(
            "nether",
            "Nether",
            "ネザー",
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.NETHER)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(3f, 3f)
                .sound(SoundType.NETHER_ORE),
            HTConst.MINECRAFT.toId(HTConst.BLOCK, "netherrack"),
        )
        registerOre(
            "end",
            "End",
            "エンド",
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(4.5f, 9f),
            HTConst.MINECRAFT.toId(HTConst.BLOCK, "end_stone"),
        )

        registrar.register("block", "%s_block") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, getter: HTPropertyGetter ->
                base * getter.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount
            }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.STORAGE_BLOCK)

            put(HTPartPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))

            addNamePattern("Block of %s", "%sブロック")
            put(HTPartPropertyKeys.FUEL_SCALE, fraction(10))
        }
        registrar.register("raw_block", "raw_%s_block") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base * 9 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.RAW_STORAGE_BLOCK)
            add(HTPartPropertyKeys.IS_RAW)

            put(HTPartPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK))

            addNamePattern("Block of Raw %s", "%sの原石ブロック")
        }
    }

    @JvmStatic
    private fun itemPart(registrar: HTMaterialPlugin.PartRegistrar) {
        registrar.register("crushed_ore", "crushed_%s_ore") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, getter: HTPropertyGetter ->
                base * getter.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER)
            }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.CRUSHED_ORE)
            add(HTPartPropertyKeys.IS_RAW)

            addNamePattern("Crushed %s Ore", "砕かれた%s鉱石")
        }
        registrar.register("dust", "%s_dust") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.DUST)

            addNamePattern("%s Dust", "%sの粉")
        }
        registrar.register("fuel", "%s_fuel") {
            put(HTPartPropertyKeys.FUEL_SCALE, fraction(1))
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.FUEL)
        }
        registrar.register("gear", "%s_gear") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base * 4 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.GEAR)

            addNamePattern("%s Gear", "%sの歯車")
        }
        registrar.register("gem", "%s_gem") {
            put(HTPartPropertyKeys.FUEL_SCALE, fraction(1))
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.GEM)
        }
        registrar.register("ingot", "%s_ingot") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.INGOT)

            addNamePattern("%s Ingot", "%sインゴット")
        }
        registrar.register("nugget", "%s_nugget") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base / 9 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.NUGGET)

            addNamePattern("%s Nugget", "%sナゲット")
            put(HTPartPropertyKeys.FUEL_SCALE, fraction(1, 10))
        }
        registrar.register("pearl", "%s_pearl") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.PEARL)

            put(HTPartPropertyKeys.FUEL_SCALE, fraction(1))
        }
        registrar.register("plate", "%s_plate") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.PLATE)

            addNamePattern("%s Plate", "%sの板")
        }
        registrar.register("raw", "raw_%s") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.RAW_MATERIALS)
            add(HTPartPropertyKeys.IS_RAW)

            addNamePattern("Raw %s", "%sの原石")
        }
        registrar.register("rod", "%s_rod") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base / 2 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.ROD)

            addNamePattern("%s Rod", "%sの棒")
        }
        registrar.register("scrap", "%s_scrap") {
            add(HTPartPropertyKeys.IS_RAW)

            addNamePattern("%s Scrap", "%sの欠片")
        }
        registrar.register("tiny", "tiny_%s") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base / 8 }
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.TINY)

            addNamePattern("Tiny %s", "小さな%s")
            put(HTPartPropertyKeys.FUEL_SCALE, fraction(1, 8))
        }
        registrar.register("wire", "%s_wire") {
            put(HTPartPropertyKeys.TAG_PREFIX, CommonTagPrefixes.WIRE)

            addNamePattern("%s Wire", "%sのワイヤ")
        }
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        fuel(provider)
        mineral(provider)
        gem(provider)
        metal(provider)
        alloy(provider)
        other(provider)
    }

    private val materialBlockSet: Set<HTPartLike> by lazy {
        setOf(
            CommonParts.ORE,
            CommonParts.ORE_DEEPSLATE,
            CommonParts.ORE_NETHER,
            CommonParts.ORE_END,
            CommonParts.BLOCK,
            CommonParts.RAW_BLOCK,
        )
    }

    @JvmStatic
    private val oreSet: Set<HTPartLike> by lazy { setOf(CommonParts.DUST, CommonParts.RAW, CommonParts.CRUSHED_ORE) }

    @JvmStatic
    private val metalSet: Set<HTPartLike> by lazy { oreSet.plus(CommonParts.INGOT).plus(CommonParts.NUGGET) }

    @JvmStatic
    private val alloySet: Set<HTPartLike> by lazy { metalSet.minus(CommonParts.RAW).minus(CommonParts.CRUSHED_ORE) }

    @JvmStatic
    private val partSet: Set<HTPartLike> by lazy { setOf(CommonParts.GEAR, CommonParts.PLATE, CommonParts.ROD) }

    @JvmStatic
    private fun fuel(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(CommonMaterialKeys.COAL_COKE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.FUEL)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(CommonParts.DUST, CommonParts.FUEL, CommonParts.TINY)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)

            setName("Coal Coke", "石炭コークス")
            setTextureSet("fuel")
            put(HTMaterialPropertyKeys.FUEL_TIME, 20 * 10 * 16)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
    }

    @JvmStatic
    private fun mineral(builder: HTMaterialPlugin.MaterialProvider) {
        fun register(
            key: HTMaterialKey,
            enName: String,
            jaName: String,
            builderAction: HTPropertyMap.Builder.() -> Unit = {},
        ) {
            builder.getBuilder(key).apply {
                setDefaultPart(HTDefaultPart.Prefixed.GEM)
                addBlockPrefixes(materialBlockSet.minus(CommonParts.BLOCK))
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
    private fun gem(builder: HTMaterialPlugin.MaterialProvider) {
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
    private fun metal(builder: HTMaterialPlugin.MaterialProvider) {
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
                addBlockPrefixes(CommonParts.BLOCK)
                addFluidPrefixes(HTFluidPart.MOLTEN)
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
            addBlockPrefixes(CommonParts.BLOCK)
            addFluidPrefixes(HTFluidPart.MOLTEN)
            addItemPrefixes(alloySet.plus(CommonParts.WIRE))
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
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Nickel", "ニッケル")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        builder.getBuilder(CommonMaterialKeys.ZINC).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(HTFluidPart.MOLTEN)
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
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Silver", "銀")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        builder.getBuilder(CommonMaterialKeys.TIN).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(materialBlockSet)
            addFluidPrefixes(HTFluidPart.MOLTEN)
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
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Lead", "鉛")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        // 7th
        register(CommonMaterialKeys.URANIUM, "Uranium", "ウラン")
        register(CommonMaterialKeys.PLUTONIUM, "Plutonium", "プルトニウム")
    }

    @JvmStatic
    private fun alloy(builder: HTMaterialPlugin.MaterialProvider) {
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
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.STEEL, toolTypes)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.MEDIUM)

            setName("Steel", "鋼鉄")
            setTextureSet(HTMaterialTextureSet.SHINE)
        }
        register(CommonMaterialKeys.INVAR, "Invar", "不変鋼")

        builder.getBuilder(CommonMaterialKeys.BRASS).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))

            setName("Brass", "真鍮")
            setTextureSet(HTMaterialTextureSet.DULL)
        }
        register(CommonMaterialKeys.CONSTANTAN, "Constantan", "コンスタンタン")
        builder.getBuilder(CommonMaterialKeys.BRONZE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addBlockPrefixes(CommonParts.BLOCK)
            addItemPrefixes(alloySet.plus(partSet))
            addToolPrefixes(HCToolMaterials.BRONZE, toolTypes)

            setName("Bronze", "青銅")
        }

        register(CommonMaterialKeys.ELECTRUM, "Electrum", "琥珀金")

        register(CommonMaterialKeys.SIGNALUM, "Signalum", "シグナルム")
        register(CommonMaterialKeys.LUMIUM, "Lumium", "ルミウム")
        register(CommonMaterialKeys.ENDERIUM, "Enderium", "エンダリウム")
    }

    @JvmStatic
    private fun other(builder: HTMaterialPlugin.MaterialProvider) {
        builder.getBuilder(CommonMaterialKeys.ASH).apply {
            addItemPrefixes(CommonParts.DUST)
            put(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.NONE)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Ash", "灰")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.STEEL.getId())
        }
        builder.getBuilder(CommonMaterialKeys.CARBON).apply {
            addItemPrefixes(CommonParts.DUST, CommonParts.PLATE, CommonParts.ROD)
            put(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Carbon", "炭素")
            // addCustomName(CommonPartsN.WIRE, "Carbon Fiber", "炭素繊維")
            setTextureSet("mineral", HTMaterialTextureSet.DULL)
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, VanillaMaterialKeys.COAL.getId())
        }
        builder.getBuilder(CommonMaterialKeys.PLASTIC).apply {
            setDefaultPart(
                HiiragiCoreTags.Items.PLASTICS,
                CommonParts.PLATE.createId(CommonMaterialKeys.PLASTIC).toItemLike(),
            )
            addBlockPrefixes(CommonParts.BLOCK)
            addFluidPrefixes(HTFluidPart.MOLTEN)
            addItemPrefixes(CommonParts.PLATE, CommonParts.ROD)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Plastic", "プラスチック")
            // addCustomName(CommonPartsN.DUST, "Plastic Pulp", "プラスチックパルプ")
            addCustomName(CommonParts.INGOT, "Plastic Bar", "プラスチックバー")
            addCustomName(CommonParts.PLATE, "Plastic Sheet", "プラスチックシート")
            // addCustomName(CommonPartsN.WIRE, "Synthetic Fiber", "合成繊維")
            setTextureSet("polymer")
        }
        builder.getBuilder(CommonMaterialKeys.RUBBER).apply {
            setDefaultPart(HiiragiCoreTags.Items.RUBBERS, HCItems.CURED_RUBBER)
            addBlockPrefixes(CommonParts.BLOCK)
            addFluidPrefixes(HTFluidPart.MOLTEN)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING

            setName("Rubber", "ゴム")
            setTextureSet("polymer", HTMaterialTextureSet.DULL)
        }
    }
}
