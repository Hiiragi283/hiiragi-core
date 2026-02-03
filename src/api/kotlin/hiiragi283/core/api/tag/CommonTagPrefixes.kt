package hiiragi283.core.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.div
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.tag.property.addNamePattern
import hiiragi283.core.api.times
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[HTTagPrefix]の一覧をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object CommonTagPrefixes {
    //    Block    //

    @JvmField
    val ORE: HTTagPrefix = HTTagPrefix.create("ore", "ores", "ores/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_ore")

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
    val BLOCK: HTTagPrefix = HTTagPrefix.create("block", "storage_blocks", "storage_blocks/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_block")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Fraction, properties: HTPropertyMap ->
            base * properties.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK).baseCount
        }

        put(HTTagPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))

        addNamePattern("Block of %s", "%sブロック")
    }

    @JvmField
    val RAW_BLOCK: HTTagPrefix = HTTagPrefix.create("raw_block", "storage_blocks", "storage_blocks/raw_%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "raw_%s_block")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base * 9 }

        put(HTTagPropertyKeys.BLOCK_PROP, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK))

        addNamePattern("Block of Raw %s", "%sの原石ブロック")
    }

    //    Item    //

    /**
     * @since 0.8.0
     */
    @JvmField
    val CROP: HTTagPrefix = HTTagPrefix.create("crop", "crops", "crops/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s")
    }

    /**
     * @since 0.9.0
     */
    @JvmField
    val CRUSHED_ORE: HTTagPrefix = HTTagPrefix.create("crushed_ore", "crushed_ores", "crushed_ores/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "crushed_%s_ore")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Fraction, properties: HTPropertyMap ->
            base * properties.getOrDefault(HTMaterialPropertyKeys.ORE_RESULT_MULTIPLIER)
        }

        addNamePattern("Crushed %s Ore", "砕かれた%s鉱石")
    }

    /**
     * @since 0.8.0
     */
    @JvmField
    val DOUGH: HTTagPrefix = HTTagPrefix.create("dough", "doughs", "doughs/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_dough")

        addNamePattern("%s Dough", "%s粉の生地")
    }

    @JvmField
    val DUST: HTTagPrefix = HTTagPrefix.create("dust", "dusts", "dusts/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_dust")

        addNamePattern("%s Dust", "%sの粉")
    }

    /**
     * @since 0.8.0
     */
    @JvmField
    val FLOUR: HTTagPrefix = HTTagPrefix.create("flour", "flours", "flours/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_flour")

        addNamePattern("%s Flour", "%s粉")
    }

    @JvmField
    val FUEL: HTTagPrefix = HTTagPrefix.create("fuel", "fuels", "%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_fuel")
    }

    @JvmField
    val GEAR: HTTagPrefix = HTTagPrefix.create("gear", "gears", "gears/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_gear")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base * 4 }

        addNamePattern("%s Gear", "%sの歯車")
    }

    @JvmField
    val GEM: HTTagPrefix = HTTagPrefix.create("gem", "gems", "gems/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_gem")
    }

    @JvmField
    val INGOT: HTTagPrefix = HTTagPrefix.create("ingot", "ingots", "ingots/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_ingot")

        addNamePattern("%s Ingot", "%sインゴット")
    }

    @JvmField
    val NUGGET: HTTagPrefix = HTTagPrefix.create("nugget", "nuggets", "nuggets/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_nugget")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base / 9 }

        addNamePattern("%s Nugget", "%sナゲット")
    }

    @JvmField
    val PEARL: HTTagPrefix = HTTagPrefix.create("pearl", "pearls", "%s_pearls") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_pearl")
    }

    @JvmField
    val PLATE: HTTagPrefix = HTTagPrefix.create("plate", "plates", "plates/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_plate")

        addNamePattern("%s Plate", "%sの板")
    }

    @JvmField
    val RAW: HTTagPrefix = HTTagPrefix.create("raw", "raw_materials", "raw_materials/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "raw_%s")

        addNamePattern("Raw %s", "%sの原石")
    }

    @JvmField
    val ROD: HTTagPrefix = HTTagPrefix.create("rod", "rods", "rods/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_rod")
        put(HTTagPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base / 2 }

        addNamePattern("%s Rod", "%sの棒")
    }

    @JvmField
    val SCRAP: HTTagPrefix = HTTagPrefix.create("scrap", "scraps", "scraps/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_scrap")

        addNamePattern("%s Scrap", "%sの欠片")
    }

    /**
     * @since 0.8.0
     */
    @JvmField
    val SEED: HTTagPrefix = HTTagPrefix.create("seed", "seeds", "seeds/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_seed")

        addNamePattern("%s Seed", "%sの種")
    }

    @JvmField
    val WIRE: HTTagPrefix = HTTagPrefix.create("wire", "wires", "wires/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_wire")

        addNamePattern("%s Wire", "%sのワイヤ")
    }

    @JvmStatic
    private fun createOre(
        name: String,
        enPrefix: String,
        jaPrefix: String,
        properties: BlockBehaviour.Properties,
        stoneTexture: ResourceLocation,
    ): HTTagPrefix = HTTagPrefix.create("${name}_ore", "ores", "ores/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "${name}_%s_ore")

        put(HTTagPropertyKeys.BLOCK_PROP, properties)
        put(HTTagPropertyKeys.ORE_STONE_TEX, stoneTexture)

        addNamePattern("$enPrefix %s Ore", "$jaPrefix%s鉱石")
        put(HTTagPropertyKeys.TEXTURE_ICON, "ore")
    }
}
