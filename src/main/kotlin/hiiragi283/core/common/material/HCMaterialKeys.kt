package hiiragi283.core.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey

object HCMaterialKeys {
    @JvmStatic
    private fun create(path: String): HTMaterialKey = HTMaterialKey.of((HiiragiCoreAPI.id(path)))

    //    Gems    //

    @JvmStatic
    val AZURE: HTMaterialKey = create("azure")

    @JvmStatic
    val CRIMSON_CRYSTAL: HTMaterialKey = create("crimson_crystal")

    @JvmStatic
    val WARPED_CRYSTAL: HTMaterialKey = create("warped_crystal")

    //    Pearls    //

    @JvmStatic
    val ELDRITCH: HTMaterialKey = create("eldritch")

    //    Metals    //

    @JvmStatic
    val ANCIENT_METAL: HTMaterialKey = create("ancient_metal")

    @JvmStatic
    val OMINOUS_METAL: HTMaterialKey = create("ominous_metal")

    //    Alloys    //

    @JvmStatic
    val AZURE_STEEL: HTMaterialKey = create("azure_steel")
}
