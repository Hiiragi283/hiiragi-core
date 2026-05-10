package hiiragi283.core.common.material.part

import hiiragi283.core.api.material.part.HTDeferredPart
import hiiragi283.core.api.material.part.HTPartLike

data object HCIntegrationParts {
    //    Immersive    //

    @JvmField
    val SHEETMETAL: HTPartLike = HTDeferredPart("sheetmetal")

    //    Mekanism    //

    @JvmField
    val ALLOY: HTPartLike = HTDeferredPart("alloy")

    @JvmField
    val CIRCUIT: HTPartLike = HTDeferredPart("mek_circuit")

    @JvmField
    val DIRTY_DUST: HTPartLike = HTDeferredPart("dirty_dust")

    @JvmField
    val CLUMP: HTPartLike = HTDeferredPart("clump")

    @JvmField
    val SHARD: HTPartLike = HTDeferredPart("shard")

    @JvmField
    val CRYSTAL: HTPartLike = HTDeferredPart("crystal")
}
