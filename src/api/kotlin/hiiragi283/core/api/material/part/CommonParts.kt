package hiiragi283.core.api.material.part

/**
 * 一般に使用される[HTPartLike]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
object CommonParts {
    //    Block    //

    @JvmField
    val ORE: HTPartLike = HTDeferredPart("ore")

    @JvmField
    val ORE_DEEPSLATE: HTPartLike = HTDeferredPart("ore/deepslate")

    @JvmField
    val ORE_NETHER: HTPartLike = HTDeferredPart("ore/nether")

    @JvmField
    val ORE_END: HTPartLike = HTDeferredPart("ore/end")

    @JvmField
    val BLOCK: HTPartLike = HTDeferredPart("block")

    @JvmField
    val RAW_BLOCK: HTPartLike = HTDeferredPart("raw_block")

    //    Item    //

    @JvmField
    val CRUSHED_ORE: HTPartLike = HTDeferredPart("crushed_ore")

    @JvmField
    val DUST: HTPartLike = HTDeferredPart("dust")

    @JvmField
    val FUEL: HTPartLike = HTDeferredPart("fuel")

    @JvmField
    val GEAR: HTPartLike = HTDeferredPart("gear")

    @JvmField
    val GEM: HTPartLike = HTDeferredPart("gem")

    @JvmField
    val INGOT: HTPartLike = HTDeferredPart("ingot")

    @JvmField
    val NUGGET: HTPartLike = HTDeferredPart("nugget")

    @JvmField
    val PEARL: HTPartLike = HTDeferredPart("pearl")

    @JvmField
    val PLATE: HTPartLike = HTDeferredPart("plate")

    @JvmField
    val RAW: HTPartLike = HTDeferredPart("raw")

    @JvmField
    val ROD: HTPartLike = HTDeferredPart("rod")

    @JvmField
    val SCRAP: HTPartLike = HTDeferredPart("scrap")

    @JvmField
    val TINY: HTPartLike = HTDeferredPart("tiny")

    @JvmField
    val WIRE: HTPartLike = HTDeferredPart("wire")
}
