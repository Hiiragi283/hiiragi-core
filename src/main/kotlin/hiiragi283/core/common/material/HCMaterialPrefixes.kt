package hiiragi283.core.common.material

import hiiragi283.core.api.material.prefix.HTMaterialPrefix

object HCMaterialPrefixes {
    //    Block    //

    @JvmField
    val ORE = HTMaterialPrefix("ore")

    @JvmField
    val ORE_DEEPSLATE = HTMaterialPrefix("ore/deepslate", "deepslate_%s_ore", "c:ores")

    @JvmField
    val ORE_NETHER = HTMaterialPrefix("ore/nether", "nether_%s_ore", "c:ores")

    @JvmField
    val ORE_END = HTMaterialPrefix("ore/end", "end_%s_ore", "c:ores")

    @JvmField
    val GLASS_BLOCK = HTMaterialPrefix("glass_block")

    @JvmField
    val STORAGE_BLOCK = HTMaterialPrefix("storage_block", "%s_block")

    @JvmField
    val STORAGE_BLOCK_RAW =
        HTMaterialPrefix("storage_block/raw", "raw_%s_block", "c:storage_blocks", "c:storage_blocks/raw_%s")

    //    Item    //

    @JvmField
    val DUST = HTMaterialPrefix("dust")

    @JvmField
    val FUEL = HTMaterialPrefix("fuel", "%s")

    @JvmField
    val GEAR = HTMaterialPrefix("gear")

    @JvmField
    val GEM = HTMaterialPrefix("gem", "%s")

    @JvmField
    val INGOT = HTMaterialPrefix("ingot")

    @JvmField
    val NUGGET = HTMaterialPrefix("nugget")

    @JvmField
    val PEARL = HTMaterialPrefix("pearl")

    @JvmField
    val PLATE = HTMaterialPrefix("plate")

    @JvmField
    val RAW_MATERIAL = HTMaterialPrefix("raw_material", "raw_%s")

    @JvmField
    val ROD = HTMaterialPrefix("rod")

    @JvmField
    val SCRAP = HTMaterialPrefix("scrap")

    @JvmField
    val TINY_DUST = HTMaterialPrefix("tiny_dust", "tiny_%s_dust")

    @JvmField
    val WIRE = HTMaterialPrefix("wire")
}
