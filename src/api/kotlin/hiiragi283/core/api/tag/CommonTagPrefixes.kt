package hiiragi283.core.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.property.getStorageBlock
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.tag.property.addNamePattern
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTTagPrefix]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object CommonTagPrefixes {
    //    Block    //

    @JvmField
    val ORE: HTTagPrefix = HTTagPrefix.create("ore") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_ore")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "ores")
        put(HTTagPropertyKeys.TAG_PATTERN, "ores/%s")
        put(
            HTTagPropertyKeys.BLOCK_PROP,
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3f, 3f),
        )
        put(HTTagPropertyKeys.ORE_STONE_TEX, HTConst.MINECRAFT.toId(HTConst.BLOCK, "stone"))

        addNamePattern("%s Ore", "%s鉱石")
    }

    @JvmField
    val ORE_DEEPSLATE: HTTagPrefix = createOre(
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

    @JvmField
    val ORE_NETHER: HTTagPrefix = createOre(
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

    @JvmField
    val ORE_END: HTTagPrefix = createOre(
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

    @JvmField
    val ORES: Set<HTTagPrefix> = setOf(ORE, ORE_DEEPSLATE, ORE_NETHER, ORE_END)

    @JvmField
    val BLOCK: HTTagPrefix = HTTagPrefix.create("block") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_block")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "storage_blocks")
        put(HTTagPropertyKeys.TAG_PATTERN, "storage_blocks/%s")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Int, properties: HTPropertyMap -> base * properties.getStorageBlock().baseCount }

        put(HTTagPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))

        addNamePattern("Block of %s", "%sブロック")
    }

    @JvmField
    val RAW_BLOCK: HTTagPrefix = HTTagPrefix.create("raw_block") {
        put(HTTagPropertyKeys.ID_PATTERN, "raw_%s_block")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "storage_blocks")
        put(HTTagPropertyKeys.TAG_PATTERN, "storage_blocks/raw_%s")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Int, _ -> base * 9 }

        put(HTTagPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK))

        addNamePattern("Block of Raw %s", "%sの原石ブロック")
    }

    //    Item    //

    @JvmField
    val DUST: HTTagPrefix = HTTagPrefix.create("dust") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_dust")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "dusts")
        put(HTTagPropertyKeys.TAG_PATTERN, "dusts/%s")

        addNamePattern("%s Dust", "%sの粉")
    }

    @JvmField
    val FUEL: HTTagPrefix = HTTagPrefix.create("fuel") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_fuel")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "fuels")
        put(HTTagPropertyKeys.TAG_PATTERN, "fuels/%s")
    }

    @JvmField
    val GEAR: HTTagPrefix = HTTagPrefix.create("gear") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_gear")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "gears")
        put(HTTagPropertyKeys.TAG_PATTERN, "gears/%s")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Int, _ -> base * 4 }

        addNamePattern("%s Gear", "%sの歯車")
    }

    @JvmField
    val GEM: HTTagPrefix = HTTagPrefix.create("gem") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_gem")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "gems")
        put(HTTagPropertyKeys.TAG_PATTERN, "gems/%s")
    }

    @JvmField
    val INGOT: HTTagPrefix = HTTagPrefix.create("ingot") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_ingot")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "ingots")
        put(HTTagPropertyKeys.TAG_PATTERN, "ingots/%s")

        addNamePattern("%s Ingot", "%sインゴット")
    }

    @JvmField
    val NUGGET: HTTagPrefix = HTTagPrefix.create("nugget") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_nugget")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "nuggets")
        put(HTTagPropertyKeys.TAG_PATTERN, "nuggets/%s")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Int, _ -> base / 9 }

        addNamePattern("%s Nugget", "%sナゲット")
    }

    @JvmField
    val PEARL: HTTagPrefix = HTTagPrefix.create("pearl") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_pearl")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "pearls")
        put(HTTagPropertyKeys.TAG_PATTERN, "pearls/%s")
    }

    @JvmField
    val PLATE: HTTagPrefix = HTTagPrefix.create("plate") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_plate")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "plates")
        put(HTTagPropertyKeys.TAG_PATTERN, "plates/%s")

        addNamePattern("%s Plate", "%sの板")
    }

    @JvmField
    val RAW: HTTagPrefix = HTTagPrefix.create("raw") {
        put(HTTagPropertyKeys.ID_PATTERN, "raw_%s")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "raw_materials")
        put(HTTagPropertyKeys.TAG_PATTERN, "raw_materials/%s")

        addNamePattern("Raw %s", "%sの原石")
    }

    @JvmField
    val ROD: HTTagPrefix = HTTagPrefix.create("rod") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_rod")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "rods")
        put(HTTagPropertyKeys.TAG_PATTERN, "rods/%s")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Int, _ -> base / 2 }

        addNamePattern("%s Rod", "%sの棒")
    }

    @JvmField
    val SCRAP: HTTagPrefix = HTTagPrefix.create("scrap") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_scrap")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "scraps")
        put(HTTagPropertyKeys.TAG_PATTERN, "scraps/%s")

        addNamePattern("%s Scrap", "%sの欠片")
    }

    @JvmField
    val WIRE: HTTagPrefix = HTTagPrefix.create("wire") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_wire")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "wires")
        put(HTTagPropertyKeys.TAG_PATTERN, "wires/%s")

        addNamePattern("%s Wire", "%sのワイヤ")
    }

    @JvmStatic
    private fun createOre(
        name: String,
        enPrefix: String,
        jaPrefix: String,
        properties: BlockBehaviour.Properties,
        stoneTexture: ResourceLocation,
    ): HTTagPrefix = HTTagPrefix.create("${name}_ore") {
        put(HTTagPropertyKeys.ID_PATTERN, "${name}_%s_ore")
        put(HTTagPropertyKeys.COMMON_TAG_PATTERN, "ores")
        put(HTTagPropertyKeys.TAG_PATTERN, "ores/%s")

        put(HTTagPropertyKeys.BLOCK_PROP, properties)
        put(HTTagPropertyKeys.ORE_STONE_TEX, stoneTexture)

        addNamePattern("$enPrefix %s Ore", "$jaPrefix%s鉱石")
        put(HTTagPropertyKeys.TEXTURE_ICON, "ore")
    }
}
