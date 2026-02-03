package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey

object UnusedMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey.of((HiiragiCoreAPI.id(path)))

    //    Metals    //

    @JvmStatic
    val ANCIENT_METAL: HTMaterialKey = create("ancient_metal")

    @JvmStatic
    val OMINOUS_METAL: HTMaterialKey = create("ominous_metal")
}
