package hiiragi283.core.api.tag.fluid

import hiiragi283.core.api.data.lang.HTLangPatternProvider

object CommonFluidTagPrefixes {
    @JvmField
    val MOLTEN = HTFluidTagPrefix("molten", "molten_%s", "molten_%s", HTLangPatternProvider.create("Molten %s", "溶融%s"))
}
