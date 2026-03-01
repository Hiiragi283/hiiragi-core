package hiiragi283.core.api.tag

object CommonTagPrefixes {
    //    Block    //

    @JvmField
    val ORE = HTTagPrefix("ores", "ores/%s")

    @JvmField
    val STORAGE_BLOCK = HTTagPrefix("storage_blocks", "storage_blocks/%s")

    @JvmField
    val RAW_STORAGE_BLOCK = HTTagPrefix("storage_blocks", "storage_blocks/raw_%s")

    //    Item    //

    @JvmField
    val CRUSHED_ORE = HTTagPrefix("crushed_ores", "crushed_ores/%s")

    @JvmField
    val DUST = HTTagPrefix("dusts", "dusts/%s")

    @JvmField
    val FUEL = HTTagPrefix("fuel", "%s")

    @JvmField
    val GEAR = HTTagPrefix("gears", "gears/%s")

    @JvmField
    val GEM = HTTagPrefix("gem", "gems/%s")

    @JvmField
    val INGOT = HTTagPrefix("ingots", "ingots/%s")

    @JvmField
    val NUGGET = HTTagPrefix("nuggets", "nuggets/%s")

    @JvmField
    val PEARL = HTTagPrefix("pearls", "%s_pearls")

    @JvmField
    val PLATE = HTTagPrefix("plates", "plates/%s")

    @JvmField
    val RAW_MATERIALS = HTTagPrefix("raw_materials", "raw_materials/%s")

    @JvmField
    val ROD = HTTagPrefix("rods", "rods/%s")

    @JvmField
    val TINY = HTTagPrefix("tiny", "tiny/%s")

    @JvmField
    val WIRE = HTTagPrefix("wires", "wires/%s")
}
