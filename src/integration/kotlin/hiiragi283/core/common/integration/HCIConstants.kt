package hiiragi283.core.common.integration

import net.neoforged.fml.ModList

object HCIConstants {
    const val AE2: String = "ae2"
    const val CREATE: String = "create"
    const val ENDER_IO = "ender_io"
    const val IMMERSIVE = "immersiveengineering"
    const val JUST_DIRE: String = "justdirethings"
    const val MEKANISM: String = "mekanism"
    const val ORITECH: String = "oritech"
    const val REPLICATION: String = "replication"

    @JvmStatic
    fun isLoaded(modId: String): Boolean = ModList.get().isLoaded(modId)
}
