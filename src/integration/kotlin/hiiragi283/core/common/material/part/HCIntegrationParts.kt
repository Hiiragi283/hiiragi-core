package hiiragi283.core.common.material.part

import hiiragi283.core.api.material.part.HTPartKey

data object HCIntegrationParts {
    //    Immersive    //

    @JvmField
    val SHEETMETAL = HTPartKey("sheetmetal")

    //    Mekanism    //

    @JvmField
    val ALLOY = HTPartKey("alloy")

    @JvmField
    val CIRCUIT = HTPartKey("mek_circuit")

    @JvmField
    val DIRTY_DUST = HTPartKey("dirty_dust")

    @JvmField
    val CLUMP = HTPartKey("clump")

    @JvmField
    val SHARD = HTPartKey("shard")

    @JvmField
    val CRYSTAL = HTPartKey("crystal")
}
