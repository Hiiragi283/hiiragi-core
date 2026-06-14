package hiiragi283.lib.material

/**
 * 共通の部品について[HTMaterialPartKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object CommonPartKeys {
    @JvmField
    val DUST = HTMaterialPartKey("dust")

    @JvmField
    val FUEL = HTMaterialPartKey("fuel")

    @JvmField
    val GEAR = HTMaterialPartKey("gear")

    @JvmField
    val GEM = HTMaterialPartKey("gem")

    @JvmField
    val INGOT = HTMaterialPartKey("ingot")

    @JvmField
    val MISC = HTMaterialPartKey("misc")

    @JvmField
    val NUGGET = HTMaterialPartKey("nugget")

    /**
     * @since 26.1.1
     */
    @JvmField
    val ORE = HTMaterialPartKey("ore")

    /**
     * @since 26.1.1
     */
    @JvmField
    val ORE_DEEPSLATE = HTMaterialPartKey("ore/deepslate")

    /**
     * @since 26.1.1
     */
    @JvmField
    val ORE_NETHER = HTMaterialPartKey("ore/nether")

    /**
     * @since 26.1.1
     */
    @JvmField
    val ORE_END = HTMaterialPartKey("ore/end")

    @JvmField
    val RAW = HTMaterialPartKey("raw")

    @JvmField
    val RAW_BLOCK = HTMaterialPartKey("raw_block")

    @JvmField
    val ROD = HTMaterialPartKey("rod")

    @JvmField
    val STORAGE_BLOCK = HTMaterialPartKey("storage_block")
}
